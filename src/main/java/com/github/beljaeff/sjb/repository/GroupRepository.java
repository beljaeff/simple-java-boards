package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.GroupCondition;
import com.github.beljaeff.sjb.model.Group;

import java.util.List;

public interface GroupRepository extends ListableRepository<Group, GroupCondition> {
    List<Group> getListByUsers(List<Integer> userIds);
}