package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.UserCondition;
import com.github.beljaeff.sjb.model.User;

public interface UserRepository extends PageableRepository<User, UserCondition> {
    User getUserByCondition(UserCondition condition, String entityGraphName);
}