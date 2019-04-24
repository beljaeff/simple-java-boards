package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.condition.TopicCondition;
import com.github.beljaeff.sjb.repository.jpa.TopicRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: position tests
class TopicRepositoryTest extends AbstractPageableRepositoryTest<Topic, TopicCondition> {

    @Autowired
    private TopicRepository repository;

    @Override
    public Class<Topic> getEntityClass() {
        return Topic.class;
    }

    @Override
    public Topic getEntity() {
        return new Topic();
    }

    @Override
    protected TopicCondition getCondition() {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(4);
        return condition;
    }

    @Override
    protected PageableRepository<Topic, TopicCondition> getRepository() {
        return repository;
    }

    @Override
    public PageableRepository<Topic, TopicCondition> getMockRepository() {
        return new TopicRepositoryJpa();
    }

    @Override
    protected String getEntityGraph() {
        return null;
    }

    @Test
    public void testGetAllByBoardAndDateSort() {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(2);
        verifyLists(condition, 12, 11, 10);
    }

    @Test
    public void testGetAllByBoardAndStickySort() {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(3);
        verifyLists(condition, 13, 15, 14);
    }

    @Test
    public void testGetAllByBoardAndTitleSort() {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(7);
        verifyLists(condition, 19, 18);
    }

    @Test
    public void testGetAllIdNotExists() {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(1000);
        List<Topic> list = getRepository().getList(condition, null);
        assertEquals(0, list.size());
    }

    /**
     *  When delete topic all posts inside it have to be deleted too.
     *  It is done by database cascading.
     *  Attachments for posts have to be deleted from db separately (no db cascading). It have to be
     *  initiated by service layer for boards, topics and posts before db deleting along with file deleting.
     *  When delete topic topic and post counts for parent board chain of this topic have to be
     *  recalculated. It is done by db trigger 'recalc-counts'.
     */
    @Transactional
    @Test
    public void testDeleteTopic() {
        int id = 17;
        Topic topic = repository.get(id);
        List<Board> parentBoards = getParentBoardsForCounts(topic.getBoard(), 1, topic.getPostsCount());

        assertTrue(repository.delete(id));
        entityManager.flush();
        entityManager.clear();

        // Verify topic deleted
        assertNull(repository.get(id));

        // Verify boards topic and post counts recalculated (trigger db side)
        verifyCounts(parentBoards);

        // Verify cascade delete properly configured and works (DB side)

        // Verify posts deleted
        assertNull(entityManager.find(Post.class, 47));
    }

    /**
     * When delete topic if this topic inside last topic field of the board - this field will be
     * nulled (db side on delete cascade set null)
     */
    @Transactional
    @Test
    public void testEnsureLastTopicIdNulledWhenDelete() {
        repository.delete(10);
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Board.class, 1).getLastTopic());
        assertNull(entityManager.find(Board.class, 2).getLastTopic());
    }

    /**
     *  When add topic topic counts for parent board chain of this topic have to be
     *  recalculated. It is done by db trigger 'recalc-counts'.
     */
    @Transactional
    @Test
    public void testAdd() {
        int boardId = 4;
        User author = entityManager.find(User.class, 55);
        Board board = entityManager.find(Board.class, boardId);
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(boardId);
        int oldSize = repository.getList(condition, null).size();

        Topic topic = new Topic();
        topic.setAuthor(author);
        topic.setBoard(board);
        topic.setTitle("title");
        topic.setIcon("icon");
        topic.setIsActive(true);
        topic.setIsApproved(true);

        List<Board> parentBoards = getParentBoardsForCounts(board, -1, 0);

        repository.add(topic);
        entityManager.flush();
        entityManager.clear();

        // Verify boards topic and post counts recalculated (trigger db side)
        verifyCounts(parentBoards);

        List<Topic> topics = repository.getList(condition, null);
        assertNotNull(topics);
        assertEquals(oldSize+1, topics.size());
    }

    @Test
    public void testUpdate() {
        int id = 19;
        Topic topic = repository.get(id);
        topic.setTitle("title");
        repository.update(topic);
        assertEquals(topic.getTitle(), repository.get(id).getTitle());
    }
}