package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import com.github.beljaeff.sjb.repository.condition.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration({"classpath:context/layer/persistence.xml"})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Sql(
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = {
        "classpath:sql/drop-tables.sql",
        "classpath:liquibase/changelogs/initial/create-tables.sql",
        "classpath:liquibase/changelogs/initial/populate-data.sql",
        "classpath:sql/populate-test-data.sql",
        "classpath:liquibase/changelogs/initial/create-constraints.sql"
})
@Sql(
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    scripts = {
            "classpath:liquibase/changelogs/initial/functions/is_attachment_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_category_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_board_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_entity_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_group_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_pm_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_post_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_topic_active.sql",
            "classpath:liquibase/changelogs/initial/functions/is_user_active.sql",
            "classpath:liquibase/changelogs/initial/functions/get_entity_ancestors.sql",
            "classpath:liquibase/changelogs/initial/triggers/recalc_counts.sql",
            "classpath:liquibase/changelogs/initial/triggers/update_topic_on_delete_post.sql"
    },
    config = @SqlConfig(separator = ScriptUtils.EOF_STATEMENT_SEPARATOR)
)
public abstract class AbstractRepositoryTest<T extends IdentifiedActiveEntity, S extends Condition> {
    @Mock
    protected EntityManager mockEntityManager;
    @InjectMocks
    protected ListableRepository<T, S> mockRepository = getMockRepository();

    @PersistenceContext
    protected EntityManager entityManager;

    abstract protected Class<T> getEntityClass();
    abstract protected T getEntity();
    abstract protected S getCondition();

    abstract protected ListableRepository<T, S> getRepository();
    abstract protected ListableRepository<T, S> getMockRepository();

    /**
     * Verify parent boards have correct topicsCount and postsCount (trigger, DB side)
     */
    protected void verifyCounts(List<Board> parentBoards) {
        for(Board b : parentBoards) {
            Board boardFromDb = entityManager.find(Board.class, b.getId());
            assertEquals(b.getPostsCount(), boardFromDb.getPostsCount());
            assertEquals(b.getTopicsCount(), boardFromDb.getTopicsCount());
        }
    }

    @Transactional
    protected List<Board> getParentBoardsForCounts(Board boardGiven, long tCount, long mCount) {
        Board board = boardGiven;
        List<Board> parentBoards = new ArrayList<>();
        for(; board != null; board=board.getParentBoard()) {
            Board b = new Board();
            b.setId(board.getId());
            b.setTopicsCount(board.getTopicsCount() - tCount);
            b.setPostsCount(board.getPostsCount() - mCount);
            parentBoards.add(b);
        }
        return parentBoards;
    }

    protected void verifyLists(S condition, int...ids) {
        verifyListsWithGraph(condition, null, ids);
    }

    protected void verifyListsWithGraph(S condition, String entityGraphName, int...ids) {
        List<T> list = getRepository().getList(condition, entityGraphName);
        List<T> expected = new ArrayList<>();
        for(int eId : ids) {
            expected.add(entityManager.find(getEntityClass(), eId));
        }
        assertThat(list).isEqualTo(expected);
    }

    @Test
    public void testGetAllConditionIsNull() {
        Executable closure = () -> getRepository().getList(null, null);
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void testGetNotExistingElement() {
        assertNull(getRepository().get(-100));
    }

    // Tests with id=1 here because we have category#id=1, board#id=1, topic#id=1, post#id=1, user#id=1
    @Test
    public void testGetElement() {
        int id = 1;
        T element = getRepository().get(id);
        T expected = entityManager.find(getEntityClass(), id);
        assertThat(element).isEqualTo(expected);
    }

    @Test
    public void testGetElementWithEntityGraph() {
        int id = 1;
        String entityGraphName = "entityGraphName";
        Map<String, Object> hints = new HashMap<>();
        hints.put(EntityGraphNamesHelper.HINT_FETCH, null);

        mockRepository.get(id, entityGraphName);
        verify(mockEntityManager, times(1)).getEntityGraph(entityGraphName);
        verify(mockEntityManager, times(1)).find(getEntityClass(), id, hints);
    }

    @Test
    public void testGetElementCheckCallRealGet() {
        int id = 1;
        BaseRepository<T, S> spyRepository = spy(mockRepository);
        spyRepository.get(id);
        verify(spyRepository, times(1)).get(id, null);
    }

    @Test
    public void testDeleteNotExistingElement() {
        assertFalse(getRepository().delete(-100));
    }

    @Test
    public void testDeleteElementByObject() {
        int id = 1000;
        T entity = getEntity();
        entity.setId(id);
        BaseRepository<T, S> spyRepository = spy(mockRepository);
        spyRepository.delete(entity);
        verify(spyRepository, times(1)).delete(id);
    }

    @Test
    public void testDeleteSomethingGoesWrong() {
        int id = -100;
        when(mockEntityManager.find(getEntityClass(), id)).thenReturn(getEntity());
        assertFalse(mockRepository.delete(id));
    }

    //TODO: refactoiring
/*
    @SuppressWarnings("unchecked")
    @Test
    void testGetAllEntityGraphSet() {
        ReflectionTestUtils.setField(mockRepository, "entityManager", mockEntityManager);
        String entityGraphName = EntityGraphs.POST_EXCEPT_TOPIC_AND_CHILD_POSTS;
        TypedQuery<Post> typedQuery = mock(TypedQuery.class);
        EntityGraph graph = mock(EntityGraph.class);
        when(mockEntityManager.getCriteriaBuilder()).thenReturn(entityManager.getCriteriaBuilder());
        when(mockEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(mockEntityManager.getEntityGraph(entityGraphName)).thenReturn(graph);

        mockRepository.getList(getCondition(), entityGraphName);
        verify(mockEntityManager, times(1)).getEntityGraph(entityGraphName);
        verify(typedQuery, times(1)).setHint(HINT_FETCH, graph);
    }
    */
}
