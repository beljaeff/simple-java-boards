package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.repository.BaseRepository;
import com.github.beljaeff.sjb.repository.GroupRepository;
import com.github.beljaeff.sjb.repository.condition.GroupCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.enums.BaseGroups;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractCrudService;

import java.util.List;

@Service
public class GroupServiceImpl extends AbstractCrudService<Group> implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    protected BaseRepository<Group, ? extends Condition> getRepository() {
        return groupRepository;
    }

    @Override
    public Group get(int id) {
        return groupRepository.get(id);
    }

    @Override
    public Group getByName(String name) {
        Assert.notNull(name, "group name should be set");

        GroupCondition groupCondition = new GroupCondition(BaseGroups.REGISTERED.name());
        List<Group> groups = groupRepository.getList(groupCondition, null);
        return CollectionUtils.isEmpty(groups) ? null : groups.get(0);
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.getList(new GroupCondition(), null);
    }
}
