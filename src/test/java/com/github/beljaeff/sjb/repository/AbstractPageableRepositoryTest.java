package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import com.github.beljaeff.sjb.repository.condition.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

abstract public class AbstractPageableRepositoryTest<T extends IdentifiedActiveEntity, S extends Condition> extends AbstractRepositoryTest<T, S> {

    private static final int PAGE_SIZE = 2;

    @Spy
    protected PageableRepository<T, S> mockRepository = getMockRepository();

    abstract protected PageableRepository<T, S> getMockRepository();
    abstract protected PageableRepository<T, S> getRepository();

    abstract protected String getEntityGraph();

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(mockRepository, "pageSize", PAGE_SIZE);
        ReflectionTestUtils.setField(mockRepository, "entityManager", entityManager);
    }

    // Pagination tests. All tests here because in populate-test-data.sql set 14 topics, posts and users

    //TODO: move to service
    /*
    @Test
    void testPageNotInBounds() {
        Executable closure = () -> mockRepository.getPageableList(getCondition(), -1, null);
        assertThrows(NotFoundException.class, closure);

        closure = () -> mockRepository.getPageableList(getCondition(), 1000, null);
        assertThrows(NotFoundException.class, closure);
    }
    */

    /**
     * When page is null - get all entities
     */
/*
    TODO: refactoring
    @Test
    void testPageIsNull() {
        PagedEntityList<T> ret = mockRepository.getPageableList(getCondition(), null, getEntityGraph());
        assertNotNull(ret);
        assertEquals(14, ret.getTotal());
        assertEquals(14, ret.getPageSize());
        assertEquals(1, ret.getCurrentPage());
        assertEquals(1, ret.getTotalPages());
    }
    @Test
    void testPageSizeIsNull() {
        ReflectionTestUtils.setField(mockRepository, "pageSize", null);
        runTestIncorrectPageSize();
    }

    @Test
    void testPageSizeNotPositive() {
        ReflectionTestUtils.setField(mockRepository, "pageSize", -1);
        runTestIncorrectPageSize();
    }

    private void runTestIncorrectPageSize() {
        PagedEntityList<T> ret = mockRepository.getPageableList(getCondition(), 1, getEntityGraph());
        assertNotNull(ret);
        assertEquals(14, ret.getTotal());
        assertEquals(10, ret.getPageSize());
        assertEquals(1, ret.getCurrentPage());
        assertEquals(2, ret.getTotalPages());
    }
*/

/*
   TODO: refactoring
    @Test
    void testTotalPages() {
        ReflectionTestUtils.setField(mockRepository, "pageSize", 7);
        PagedEntityList<T> ret1 = mockRepository.getPageableList(getCondition(), 1, getEntityGraph());
        assertNotNull(ret1);
        // total 14 elems and 7 per page => totalPages == 2
        assertEquals(2, ret1.getTotalPages());

        ReflectionTestUtils.setField(mockRepository, "pageSize", 5);
        PagedEntityList<T> ret2 = mockRepository.getPageableList(getCondition(), 3, getEntityGraph());
        assertNotNull(ret2);
        // total 14 elems and 5 per page => totalPages == 3 and last page (#3) has 4 elems
        assertEquals(14, ret2.getTotal());
        assertEquals(5, ret2.getPageSize());
        assertEquals(3, ret2.getCurrentPage());
        assertEquals(3, ret2.getTotalPages());
        assertEquals(4, ret2.getList().size());
    }*/

    /**
     * Test for case when there is no entities selected and page == 1
     * it have to be returned empty list and 1 total pages
     */
    /*
    TODO: refactoring
    @SuppressWarnings("unchecked")
    @Test
    void testGetPagedCheckTotalZero() {
        ReflectionTestUtils.setField(mockRepository, "entityManager", mockEntityManager);
        TypedQuery<Post> typedQueryPost = mock(TypedQuery.class);
        TypedQuery<Long> typedQueryCount = mock(TypedQuery.class);
        when(mockEntityManager.getCriteriaBuilder()).thenReturn(entityManager.getCriteriaBuilder());
        when(mockEntityManager.createQuery(any(CriteriaQuery.class)))
                .thenReturn(typedQueryPost)
                .thenReturn(typedQueryCount);
        when(typedQueryCount.getSingleResult()).thenReturn(Long.valueOf(0));

        PagedEntityList<T> actual = mockRepository.getPageableList(getCondition(), 1, getEntityGraph());
        assertEquals(0, actual.getTotal());
        assertEquals(2, actual.getPageSize());
        assertEquals(1, actual.getCurrentPage());
        assertEquals(1, actual.getTotalPages());
        assertEquals(0, actual.getList().size());
    }*/
}