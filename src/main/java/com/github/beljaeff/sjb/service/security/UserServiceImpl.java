package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.exception.UserProfileException;
import com.github.beljaeff.sjb.repository.BaseRepository;
import com.github.beljaeff.sjb.repository.UserRepository;
import com.github.beljaeff.sjb.repository.condition.UserCondition;
import com.github.beljaeff.sjb.security.UserPrincipal;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.dto.dto.PagedListDto;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.profile.AddUserGroupForm;
import com.github.beljaeff.sjb.enums.BaseGroups;
import com.github.beljaeff.sjb.mapper.UserMapper;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.EntityGraphs;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractCrudService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.beljaeff.sjb.controller.Routes.PROFILE_LIST;
import static com.github.beljaeff.sjb.enums.BasePermission.ACTIVATE_USER;
import static com.github.beljaeff.sjb.enums.BasePermission.ADMIN;
import static com.github.beljaeff.sjb.enums.ErrorCode.GROUP_DUPLICATE;
import static com.github.beljaeff.sjb.enums.ErrorCode.GROUP_NOT_FOUND;
import static com.github.beljaeff.sjb.enums.ErrorCode.USER_NOT_FOUND;
import static com.github.beljaeff.sjb.util.UserUtils.ADMIN_ID;
import static com.github.beljaeff.sjb.util.UserUtils.ANONYMOUS_ID;
import static com.github.beljaeff.sjb.util.UserUtils.hasPermission;

//TODO: when change or reset password check it is not equal last password
//TODO: tests
@Slf4j
@Service("userService")
public class UserServiceImpl extends AbstractCrudService<User> implements UserDetailsService, UserService {

    private final CommonAttachmentService commonAttachmentService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(CommonAttachmentService commonAttachmentService, GroupService groupService,
                           UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.commonAttachmentService = commonAttachmentService;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    protected BaseRepository<User, ? extends Condition> getRepository() {
        return userRepository;
    }

    private User get(int id, String entityGraphName) {
        User user = userRepository.get(id, entityGraphName);

        if(!(hasPermission(ACTIVATE_USER) || hasPermission(ADMIN)) && !user.getIsActive() ||
           !hasPermission(ADMIN) && !user.getIsActivated() ||
           !hasPermission(ADMIN) && user.getId() == ANONYMOUS_ID) {
            throw new NotFoundException();
        }

        return user;
    }

    @Override
    public User get(int id) {
        return get(id, EntityGraphs.USERS_WITH_AVATAR);
    }

    @Override
    public User getWithGroups(int id) {
        return get(id, EntityGraphs.USERS_WITH_AVATAR_AND_GROUPS);
    }

    @Override
    public User getByCondition(UserCondition condition) {
        Assert.notNull(condition, "condition should be set");
        return userRepository.getUserByCondition(condition, null);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Assert.notNull(login, "login should be set");
        UserCondition condition = UserCondition.builder()
                .nickName(login)
                .isActivated(true)
                .isActive(true)
                .build();
        User user = userRepository.getUserByCondition(condition, EntityGraphs.USERS_WITH_GROUPS_WITH_PERMISSIONS);
        if(user == null || user.getId() == ANONYMOUS_ID) {
            log.debug("Can not perform authorization for login '{}'. User not found or Anonymous.", login);
            throw new UsernameNotFoundException("User " + login + " is not found");
        }
        return new UserPrincipal(user);
    }

    @Override
    public UserPrincipal getAnonymousUser() {
        User user = userRepository.get(ANONYMOUS_ID, EntityGraphs.USERS_WITH_GROUPS_WITH_PERMISSIONS);
        if(user == null) {
            log.debug("Can not perform authorization for Anonymous user");
            throw new UsernameNotFoundException("Anonymous user is not found");
        }
        return new UserPrincipal(user);
    }

    @Override
    public String encodePassword(String password) {
        Assert.notNull(password, "password should be set");
        return passwordEncoder.encode(password);
    }

    @Override
    public List<BreadcrumbDto> getBreadcrumbs(Integer idEntity) {
        List<BreadcrumbDto> breadcrumbs = super.getBreadcrumbs(idEntity);
        breadcrumbs.add(new BreadcrumbDto(PROFILE_LIST, recordService.getText("user.profiles.text.header")));
        return breadcrumbs;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<BaseDto> getList(int page) {
        UserCondition condition = new UserCondition();
        if(!(hasPermission(ACTIVATE_USER) || hasPermission(ADMIN))) {
            condition.setIsActive(true);
        }
        if(!hasPermission(ADMIN)) {
            condition.setIsActivated(true);
        }
        if(hasPermission(ADMIN)) {
            condition.setShowAnonymous(true);
        }

        PagedEntityList<User> users = userRepository.getPageableList(condition, page, EntityGraphs.USERS_WITH_AVATAR);
        if(page < 1 || page > users.getTotalPages()) {
            log.debug("Requested user page does not exist");
            throw new NotFoundException();
        }

        BaseDto entity = new PagedListDto(userMapper.userToUserDto(users));
        return new PageDto<>(entity, getBreadcrumbs(null));
    }

    @Override
    @Transactional
    public ActionStatusDto<User> delete(int id) {
        if(id != ANONYMOUS_ID && id != ADMIN_ID) {
            ActionStatusDto<User> actionStatus = super.delete(id);
            Attachment avatar = actionStatus.getEntity().getAvatar();
            if (avatar != null) {
                commonAttachmentService.delete(avatar.getId());
            }
            return actionStatus;
        }
        log.debug("Can not delete Admin and Anonymous users");
        ActionStatusDto<User> actionStatus = new ActionStatusDto<>();
        actionStatus.setEntity(get(id));
        return actionStatus;
    }

    @Override
    @Transactional
    public ActionStatusDto<User> changeBanned(int id) {
        User entity = getRepository().get(id);
        ActionStatusDto<User> actionStatus = new ActionStatusDto<>();
        actionStatus.setEntity(entity);
        if(entity != null) {
            entity.setIsBanned(!entity.getIsBanned());
            getRepository().update(entity);
            actionStatus.setStatus(true);
        }
        return actionStatus;
    }

    @Override
    @Transactional
    public void addUserGroup(AddUserGroupForm form) {
        Assert.notNull(form, "group form should be set");

        User user = getWithGroups(form.getIdUser());
        if(user == null) {
            log.debug("User not found: error adding group {} for user {}", form, user);
            throw new UserProfileException(Collections.singletonList(USER_NOT_FOUND));
        }
        Group group = groupService.get(form.getIdGroup());
        if(group == null) {
            log.debug("Group not found: error adding group {} for user {}", group, user);
            throw new UserProfileException(Collections.singletonList(GROUP_NOT_FOUND));
        }
        if(!CollectionUtils.isEmpty(user.getGroups()) && user.getGroups().contains(group)) {
            log.debug("User already has this group: error adding group {} for user {}", group, user);
            throw new UserProfileException(Collections.singletonList(GROUP_DUPLICATE));
        }

        if(CollectionUtils.isEmpty(user.getGroups())) {
            Set<Group> groups = new HashSet<>();
            groups.add(group);
            user.setGroups(groups);
        }
        else {
            user.getGroups().add(group);
        }
        save(user);
    }

    @Override
    @Transactional
    public ActionStatusDto<User> removeUserGroup(int idUser, int idGroup) {
        User user = get(idUser, EntityGraphs.USERS_WITH_AVATAR_AND_GROUPS);
        Group group = groupService.get(idGroup);
        ActionStatusDto<User> actionStatus = new ActionStatusDto<>();
        actionStatus.setEntity(user);
        if(user == null || group == null || CollectionUtils.isEmpty(user.getGroups()) ||
           idUser == ADMIN_ID && group.getCode().equals(BaseGroups.ADMINS.name()) ||
           idUser == ANONYMOUS_ID && group.getCode().equals(BaseGroups.UNPRIVILEGED.name()) ||
           !user.getGroups().contains(group)) {
            log.debug("Error removing group {} for user {}", group, user);
            return actionStatus;
        }
        user.getGroups().remove(group);
        save(user);
        actionStatus.setStatus(true);
        return actionStatus;
    }

    @Override
    public boolean passwordsMatch(String encodedPassword, String password) {
        Assert.notNull(encodedPassword, "encoded password should be set");
        Assert.notNull(password, "password should be set");

        return passwordEncoder.matches(password, encodedPassword);
    }
}