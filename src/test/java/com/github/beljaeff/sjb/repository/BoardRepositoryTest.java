package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.repository.condition.BoardCondition;
import com.github.beljaeff.sjb.repository.jpa.BoardRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: position tests
class BoardRepositoryTest extends AbstractRepositoryTest<Board, BoardCondition> {

    @Autowired
    private BoardRepository repository;

    @Override
    public Class<Board> getEntityClass() {
        return Board.class;
    }

    @Override
    public Board getEntity() {
        return new Board();
    }

    @Override
    protected BoardCondition getCondition() {
        BoardCondition condition = new BoardCondition();
        condition.setParentId(5);
        return condition;
    }

    @Override
    protected ListableRepository<Board, BoardCondition> getRepository() {
        return repository;
    }

    @Override
    protected ListableRepository<Board, BoardCondition> getMockRepository() {
        return new BoardRepositoryJpa();
    }

    private void verifyBreadcrumbs(Integer id) {
        Integer boardId = id;
        List<CommonEntity> breadcrumbs = repository.getBreadcrumbs(boardId);
        List<CommonEntity> expected = new ArrayList<>();
        int i = 1;
        while (boardId != null) {
            Board board = repository.get(boardId);
            CommonEntity breadcrumb = new CommonEntity();
            breadcrumb.setId(i);
            breadcrumb.setIdEntity(board.getId());
            breadcrumb.setTitle(board.getTitle());
            breadcrumb.setIsActive(board.getIsActive());
            breadcrumb.setType(EntityType.BOARD);
            expected.add(0, breadcrumb);
            i++;
            if(board.getParentBoard() == null) {
                Category category = board.getCategory();
                if(category != null) {
                    breadcrumb = new CommonEntity();
                    breadcrumb.setId(i);
                    breadcrumb.setIdEntity(category.getId());
                    breadcrumb.setTitle(category.getTitle());
                    breadcrumb.setIsActive(category.getIsActive());
                    breadcrumb.setType(EntityType.CATEGORY);
                    expected.add(0, breadcrumb);
                }
                break;
            }
            boardId = board.getParentBoard().getId();
        }
        assertThat(breadcrumbs).isEqualTo(expected);
    }

    @Test
    public void testGetAllNullId() {
        BoardCondition condition = new BoardCondition();
        verifyLists(condition, 14);
    }

    @Test
    public void testGetAllNullCategoryBoardSort() {
        BoardCondition condition = new BoardCondition();
        condition.setParentId(2);
        verifyLists(condition, 8, 5, 6);
    }

    @Test
    public void testGetAllNullParentBoardSort() {
        BoardCondition condition = new BoardCondition();
        condition.setCategoryId(3);
        verifyLists(condition, 17, 16, 15);
    }

    @Test
    public void testGetAll() {
        BoardCondition condition = new BoardCondition();
        condition.setParentId(1);
        condition.setCategoryId(3);
        verifyLists(condition, 4);
    }

    @Test
    public void testGetAllParentNotExistsCategoryExists() {
        BoardCondition condition = new BoardCondition();
        condition.setParentId(1000);
        condition.setCategoryId(1);
        List<Board> boards = repository.getList(condition, null);
        assertEquals(0, boards.size());
    }

    @Test
    public void testGetAllCategoryNotExists() {
        BoardCondition condition = new BoardCondition();
        condition.setParentId(1);
        condition.setCategoryId(1000);
        List<Board> boards = repository.getList(condition, null);
        assertEquals(0, boards.size());
    }

    /**
     *  When delete board all topics inside it and all posts inside topics have to be deleted too.
     *  Also all child boards have to be deleted too with all contents.
     *  It is done by database cascading.
     *  Attachments for posts have to be deleted from db separately (no db cascading). It have to be
     *  initiated by service layer for boards, topics and posts before db deleting along with file deleting.
     *  When delete board topic and post counts for parent board chain of this board have to be
     *  recalculated. It is done by db trigger 'recalc-counts'.
     */
    @Transactional
    @Test
    public void testDeleteBoard() {
        int id = 5;
        Board board = repository.get(id);
        List<Board> parentBoards = getParentBoardsForCounts(board.getParentBoard(), board.getTopicsCount(),
                                                            board.getPostsCount());

        assertTrue(repository.delete(id));
        entityManager.flush();
        entityManager.clear();

        // Verify board deleted
        assertNull(repository.get(id));

        // Verify boards topic and post counts recalculated (trigger db side)
        verifyCounts(parentBoards);

        // Verify cascade delete properly configured and works (DB side)

        // Verify child boards deleted
        assertNull(entityManager.find(Board.class, 7));

        // Verify topics deleted
        assertNull(entityManager.find(Topic.class, 18));
        assertNull(entityManager.find(Topic.class, 19));

        // Verify posts deleted
        assertNull(entityManager.find(Post.class, 48));
        assertNull(entityManager.find(Post.class, 49));
    }

    /**
     * Check db constraint properly configured (there can be only one active board with title inside current category)
     */
    @Test
    public void testAddSame() {
        int parentId = 1;
        int categoryId = 3;
        Board board = new Board();
        board.setParentBoard(repository.get(parentId));
        board.setCategory(entityManager.find(Category.class, categoryId));
        board.setTitle("Board-top-1-3");
        board.setIcon("icon");
        board.setIsActive(true);
        Executable closure = () -> repository.add(board);
        assertThrows(DataIntegrityViolationException.class, closure);
    }

    @Test
    public void testAdd() {
        int parentId = 7;
        Board board = new Board();
        board.setParentBoard(repository.get(parentId));
        board.setTitle("title");
        board.setIcon("icon");
        board.setIsActive(true);
        repository.add(board);
        BoardCondition condition = new BoardCondition();
        condition.setParentId(parentId);
        List<Board> boards = repository.getList(condition, null);
        assertNotNull(boards);
        assertEquals(1, boards.size());
    }

    @Test
    public void testUpdate() {
        int id = 1;
        Board board = repository.get(id);
        board.setTitle("title");
        repository.update(board);
        assertEquals(board.getTitle(), repository.get(id).getTitle());
    }

    @Test
    public void testCheckActiveAncestorsNullId() {
        assertFalse(repository.isEntityActive(null, true));
    }

    @Test
    public void testCheckActiveAncestorsBoardWithNullParent() {
        assertTrue(repository.isEntityActive(1, true));
    }

    @Test
    public void testCheckActiveAncestorsInactiveBoard() {
        assertFalse(repository.isEntityActive(8, true));
    }

    @Test
    public void testCheckActiveAncestorsBoardWithInactiveParent() {
        assertFalse(repository.isEntityActive(9, true));
    }

    @Test
    public void testCheckActiveAncestorsNotExistentBoard() {
        assertFalse(repository.isEntityActive(999, true));
    }

    @Test
    public void testGetBreadCrumbsNotExistentBoard() {
        assertEquals(0, repository.getBreadcrumbs(-100).size());
    }

    @Transactional
    @Test
    public void testGetBreadCrumbsTopBoard() {
        verifyBreadcrumbs(1);
    }

    @Test
    public void testGetBreadCrumbsTopBoardWithoutCategory() {
        verifyBreadcrumbs(14);
    }

    @Transactional
    @Test
    public void testGetBreadCrumbs() {
        verifyBreadcrumbs(7);
    }
}
