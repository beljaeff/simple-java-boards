package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.condition.PostCondition;
import com.github.beljaeff.sjb.repository.jpa.PostRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: position tests
class PostRepositoryTest extends AbstractPageableRepositoryTest<Post, PostCondition> {

    @Autowired
    private PostRepository repository;

    @Override
    public Class<Post> getEntityClass() {
        return Post.class;
    }

    @Override
    public Post getEntity() {
        return new Post();
    }

    @Override
    protected String getEntityGraph() {
        return EntityGraphNamesHelper.POST_EXCEPT_TOPIC_AND_CHILD_POSTS;
    }

    @Override
    protected PostCondition getCondition() {
        PostCondition condition = new PostCondition();
        condition.setTopicId(4);
        return condition;
    }

    @Override
    protected PageableRepository<Post, PostCondition> getRepository() {
        return repository;
    }

    @Override
    public PageableRepository<Post, PostCondition> getMockRepository() {
        return new PostRepositoryJpa(null, null);
    }

    @Test
    public void testGetAllByTopicAndSort() {
        PostCondition condition = new PostCondition();
        condition.setTopicId(10);
        verifyListsWithGraph(condition, EntityGraphNamesHelper.POST_EXCEPT_TOPIC_AND_CHILD_POSTS,34, 35, 36, 31, 33, 32, 30);
    }

    @Test
    public void testGetAllIdNotExists() {
        PostCondition condition = new PostCondition();
        condition.setTopicId(1000);
        List<Post> list = getRepository().getList(condition, null);
        assertEquals(0, list.size());
    }

    /**
     * Attachments for posts have to be deleted from db separately (no db cascading). It have to be
     * initiated by service layer for boards, topics and posts before db deleting along with file deleting.
     * When delete post post count for topic and for parent board chain for this topic have to be
     * recalculated. It is done by db trigger 'recalc-counts'.
     */
    //TODO: needs refactoring
    @Transactional
    @Test
    public void testDeletePost() {
        int id = 47;
        Post post = repository.get(id);
        Topic topic = post.getTopic();
        int topicId = topic.getId();
        List<Board> parentBoards = getParentBoardsForCounts(post.getTopic().getBoard(), 0, 1);

        assertTrue(repository.delete(id));
        topic.setFirstPost(null);
        topic.setLastPost(null);

        entityManager.flush();
        entityManager.clear();

        // Verify post deleted
        assertNull(repository.get(id));

        topic = entityManager.find(Topic.class, topicId);
        // If post was the only post inside topic, first and last post fields of the topic will be nulled
        assertNull(topic.getFirstPost());
        assertNull(topic.getLastPost());

        // Verify boards topic and post counts recalculated (trigger db side)
        verifyCounts(parentBoards);
    }

    /**
     * When delete post if this post was first post of its topic
     * appropriate field of the topic recalculated by db trigger
     */
    @Transactional
    @Test
    public void testEnsureFirstPostIdRecalculatedWhenDelete() {
        repository.delete(62);
        entityManager.flush();
        entityManager.clear();

        Post actual = entityManager.find(Topic.class, 4).getFirstPost();
        Post expected = entityManager.find(Post.class, 63);
        assertEquals(expected, actual);
    }

    /**
     * When delete post if this post was last post of its topic
     * appropriate field of the topic recalculated by db trigger
     */
    @Transactional
    @Test
    public void testEnsureLastPostIdRecalculatedWhenDelete() {
        repository.delete(75);
        entityManager.flush();
        entityManager.clear();

        Post actual = entityManager.find(Topic.class, 4).getLastPost();
        Post expected = entityManager.find(Post.class, 74);
        assertEquals(expected, actual);
    }

    @Transactional
    @Test
    public void testEnsureParentPostIdNulledWhenDelete() {
        repository.delete(31);
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(Post.class, 33).getParentPost());
        assertNull(entityManager.find(Post.class, 34).getParentPost());
    }

    /**
     *  When add post post counts for topic and parent board chain of this topic have to be
     *  recalculated. It is done by db trigger 'recalc-counts'.
     */
    @Transactional
    @Test
    public void testAdd() {
        int topicId = 19;
        User author = entityManager.find(User.class, 55);
        Topic topic = entityManager.find(Topic.class, topicId);

        Post post = new Post();
        post.setTopic(topic);
        post.setIpCreate("9.9.9.9");
        post.setAuthor(author);
        post.setIpLastUpdate("9.9.9.9");
        post.setUserLastUpdate(author);
        post.setBody("body");
        post.setIsActive(true);
        post.setIsApproved(true);

        List<Board> parentBoards = getParentBoardsForCounts(topic.getBoard(), 0, -1);

        repository.add(post);
        entityManager.flush();
        entityManager.clear();

        // Verify boards topic and post counts (trigger db side)
        verifyCounts(parentBoards);

        PostCondition condition = new PostCondition();
        condition.setTopicId(topicId);
        List<Post> posts = repository.getList(condition, null);
        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    public void testUpdate() {
        int id = 49;
        Post post = repository.get(id);
        post.setBody("body");
        repository.update(post);
        assertEquals(post.getBody(), repository.get(id).getBody());
    }
}