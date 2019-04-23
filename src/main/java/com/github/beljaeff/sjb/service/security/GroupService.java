package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.service.CrudService;

import java.util.List;

public interface GroupService extends CrudService<Group> {
    Group getByName(String name);
    List<Group> getAll();
}
