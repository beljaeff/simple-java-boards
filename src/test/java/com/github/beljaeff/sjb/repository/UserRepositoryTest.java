package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.EntityGraphs;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.condition.UserCondition;
import com.github.beljaeff.sjb.repository.jpa.UserRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest extends AbstractPageableRepositoryTest<User, UserCondition> {

    @Autowired
    private UserRepository repository;

    @Override
    protected PageableRepository<User, UserCondition> getMockRepository() {
        return new UserRepositoryJpa(null);
    }

    @Override
    PageableRepository<User, UserCondition> getRepository() {
        return repository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected User getEntity() {
        return new User();
    }

    @Override
    protected UserCondition getCondition() {
        return new UserCondition();
    }

    @Override
    String getEntityGraph() {
        return EntityGraphs.USERS_WITH_AVATAR;
    }

    @Test
    void testGetAllSort() {
        // Also checks case when userCondition.groups == null
        verifyLists(new UserCondition(), 1, 0, 55, 64, 65, 66, 56, 57, 58, 59, 60, 61, 62, 63);

        // Check case when userCondition.groups == empty set
        verifyLists(new UserCondition(Collections.emptySet()), 1, 0, 55, 64, 65, 66, 56, 57, 58, 59, 60, 61, 62, 63);
    }

    /**
     * Test that
     *  countQuery.where(where);
     * in makePagination don't return NPE
     */
    @Test
    void testGetPagedConditionNull() {
        repository.getPageableList(new UserCondition(), 1, null);
    }
}
