package com.github.beljaeff.sjb.controller.profile;

import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.dto.form.profile.AddUserGroupForm;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.exception.UserProfileException;
import com.github.beljaeff.sjb.mapper.ProfileMapper;
import com.github.beljaeff.sjb.service.profile.ShowProfileService;
import com.github.beljaeff.sjb.service.security.GroupService;
import com.github.beljaeff.sjb.service.security.UserService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;

import javax.validation.Valid;
import java.util.List;

import static com.github.beljaeff.sjb.controller.RoutesHelper.PROFILE_LIST;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_ACTIVATE;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_BAN;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_DELETE;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_PROFILE_ADD_GROUP;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_PROFILE_COMMON;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_PROFILE_GROUPS;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_PROFILE_REMOVE_GROUP;
import static com.github.beljaeff.sjb.controller.RoutesHelper.USER_PROFILE_SAVE_GROUP;
import static com.github.beljaeff.sjb.util.CommonUtils.addErrors;

@Slf4j
@Controller
public class EditController extends AbstractShowController {

    private static final String ATTR_GROUPS = "groups";
    private static final String ATTR_FORM = "addUserGroupForm";
    private static final String ATTR_ACTION = "profileAction";

    private final GroupService groupService;
    private final UserService userService;
    private final ShowProfileService showProfileService;

    private final ProfileMapper profileMapper;

    @Autowired
    public EditController(GroupService groupService, UserService userService, ShowProfileService showProfileService, ProfileMapper profileMapper) {
        this.groupService = groupService;
        this.userService = userService;
        this.showProfileService = showProfileService;
        this.profileMapper = profileMapper;
    }

    @Override
    public String getHeader(ProfileDto dto) {
        String messageCode = dto.getId() == UserUtils.getCurrentUser().getId() ? "own.profile.text.header" : "profile.text.header";
        return recordService.getText(messageCode, dto.getNickName()) +
               recordService.getText(dto.getProfileSection().getCode() + ".profile.section.header.add") +
               recordService.getText("own.profile.edit.header.addition");
    }

    @Override
    protected List<BreadcrumbDto> getBreadcrumbs(ProfileDto dto) {
        List<BreadcrumbDto> breadcrumbs = super.getBreadcrumbs();
        breadcrumbs.add(new BreadcrumbDto(PROFILE_LIST, recordService.getText("user.profiles.text.header")));
        String messageCode = dto.getId() == UserUtils.getCurrentUser().getId() ? "own.profile.text.header" : "profile.text.header";
        String title = recordService.getText(messageCode, dto.getNickName()) +
                       recordService.getText(dto.getProfileSection().getCode() + ".profile.section.header.add");
        breadcrumbs.add(new BreadcrumbDto(buildSegmentsPath(USER_PROFILE_COMMON, dto.getId(), dto.getProfileSection().getCode()), title));
        return breadcrumbs;
    }

    @PreAuthorize("hasAnyPermissions('DELETE_USER', 'ADMIN')")
    @GetMapping(USER_DELETE)
    public String delete(@PathVariable int id, @PathVariable String section) {
        userService.delete(id);
        return "redirect:" + PROFILE_LIST;
    }

    @PreAuthorize("hasAnyPermissions('ACTIVATE_USER', 'ADMIN')")
    @GetMapping(USER_ACTIVATE)
    public String changeActive(@PathVariable int id, @PathVariable String section) {
        userService.changeActive(id);
        return "redirect:" + buildSegmentsPath(USER_PROFILE_COMMON, id, section);
    }

    @PreAuthorize("hasAnyPermissions('BAN_USER', 'ADMIN')")
    @GetMapping(USER_BAN)
    public String changeBanned(@PathVariable int id, @PathVariable String section) {
        userService.changeBanned(id);
        return "redirect:" + buildSegmentsPath(USER_PROFILE_COMMON, id, section);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_USER_GROUP', 'ADMIN')")
    @GetMapping(USER_PROFILE_ADD_GROUP)
    public String showAddUserGroupForm(@PathVariable int id, AddUserGroupForm form, Model model) {
        if(form != null && form.getIdUser() != id) {
            form.setIdUser(id);
        }
        model.addAttribute(ATTR_GROUPS, profileMapper.groupToGroupDto(groupService.getAll()));
        model.addAttribute(ATTR_FORM, form);
        model.addAttribute(ATTR_ACTION, "add");
        return showProfile(
                showProfileService.getGroups(id),
                ProfileSection.GROUPS,
                model
        );
    }

    @PreAuthorize("hasAnyPermissions('EDIT_USER_GROUP', 'ADMIN')")
    @PostMapping(USER_PROFILE_ADD_GROUP)
    public String checkAddUserGroupForm(@Valid AddUserGroupForm form, BindingResult result, Model model) {
        try {
            userService.addUserGroup(form);
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
            return showAddUserGroupForm(form.getIdUser(), form, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_GROUP, form.getIdUser());
    }

    @PreAuthorize("hasAnyPermissions('EDIT_USER_GROUP', 'ADMIN')")
    @GetMapping(USER_PROFILE_SAVE_GROUP)
    public String saveAddUserGroupForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_GROUPS, id);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_USER_GROUP', 'ADMIN')")
    @GetMapping(USER_PROFILE_REMOVE_GROUP)
    public String removeUserGroup(@PathVariable int id, @PathVariable int idGroup) {
        userService.removeUserGroup(id, idGroup);
        return "redirect:" + buildSegmentsPath(USER_PROFILE_GROUPS, id);
    }
}