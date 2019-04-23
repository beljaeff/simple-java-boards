package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.repository.condition.UserCondition;
import com.github.beljaeff.sjb.security.UserPrincipal;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.profile.AddUserGroupForm;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.service.CrudService;

public interface UserService extends CrudService<User> {

    String encodePassword(String password);

    boolean passwordsMatch(String encodedPassword, String password);

    UserPrincipal getAnonymousUser();

    User getWithGroups(int id);

    PageDto<BaseDto> getList(int page);

    User getByCondition(UserCondition condition);

    ActionStatusDto<User> changeBanned(int id);

    void addUserGroup(AddUserGroupForm form);

    ActionStatusDto<User> removeUserGroup(int userId, int groupId);
}