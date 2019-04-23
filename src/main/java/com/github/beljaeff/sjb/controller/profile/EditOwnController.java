package com.github.beljaeff.sjb.controller.profile;

import com.github.beljaeff.sjb.dto.dto.profile.ProfileDto;
import com.github.beljaeff.sjb.dto.form.profile.ChangeEmailForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangePasswordForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangeSecretAnswerForm;
import com.github.beljaeff.sjb.dto.form.profile.EditAvatarForm;
import com.github.beljaeff.sjb.dto.form.profile.EditOverviewForm;
import com.github.beljaeff.sjb.enums.Gender;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.exception.ForbiddenException;
import com.github.beljaeff.sjb.exception.UserProfileException;
import com.github.beljaeff.sjb.mapper.ProfileMapper;
import com.github.beljaeff.sjb.service.profile.EditOwnProfileService;
import com.github.beljaeff.sjb.service.profile.ShowProfileService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.util.Utils;

import javax.validation.Valid;
import java.util.List;

import static com.github.beljaeff.sjb.controller.Routes.PROFILE_LIST;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_CHANGE_EMAIL;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_CHANGE_PASSWORD;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_CHANGE_SECRET_ANSWER;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_COMMON;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_EDIT_AVATAR;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_EDIT_OVERVIEW;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_OVERVIEW;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_REMOVE_AVATAR;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SAVE_AVATAR;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SAVE_EMAIL;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SAVE_OVERVIEW;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SAVE_PASSWORD;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SAVE_SECRET_ANSWER;
import static com.github.beljaeff.sjb.controller.Routes.USER_PROFILE_SECURITY;
import static com.github.beljaeff.sjb.util.Utils.addErrors;

@Slf4j
@PreAuthorize("hasPermission('EDIT_OWN_PROFILE')")
@Controller
public class EditOwnController extends AbstractShowController {

    private static final String ATTR_GENDER_LIST = "genderList";
    private static final String ATTR_ACTIVE_LIST = "activeList";
    private static final String ATTR_CHANGE_EMAIL_FORM = "changeEmailForm";
    private static final String ATTR_CHANGE_PASSWORD_FORM = "changePasswordForm";
    private static final String ATTR_CHANGE_SECRET_ANSWER_FORM = "changeSecretAnswerForm";
    private static final String ATTR_EDIT_OVERVIEW_FORM = "editOverviewForm";
    private static final String ATTR_EDIT_AVATAR_FORM = "editAvatarForm";
    private static final String ATTR_ACTION = "profileAction";

    private final ShowProfileService showProfileService;
    private final EditOwnProfileService editProfileService;

    private final ProfileMapper profileMapper;

    @Autowired
    public EditOwnController(ShowProfileService showProfileService, EditOwnProfileService editProfileService, ProfileMapper profileMapper) {
        this.showProfileService = showProfileService;
        this.editProfileService = editProfileService;
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
    List<BreadcrumbDto> getBreadcrumbs(ProfileDto dto) {
        List<BreadcrumbDto> breadcrumbs = super.getBreadcrumbs();
        breadcrumbs.add(new BreadcrumbDto(PROFILE_LIST, recordService.getText("user.profiles.text.header")));
        String messageCode = dto.getId() == UserUtils.getCurrentUser().getId() ? "own.profile.text.header" : "profile.text.header";
        String title = recordService.getText(messageCode, dto.getNickName()) +
                       recordService.getText(dto.getProfileSection().getCode() + ".profile.section.header.add");
        breadcrumbs.add(new BreadcrumbDto(buildSegmentsPath(USER_PROFILE_COMMON, dto.getId(), dto.getProfileSection().getCode()), title));
        return breadcrumbs;
    }

    @GetMapping(USER_PROFILE_CHANGE_EMAIL)
    public String showChangeEmailForm(@PathVariable int id, ChangeEmailForm form, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Change email request: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        if(form != null && StringUtils.isEmpty(form.getEmail())) {
            form.setEmail(UserUtils.getCurrentUser().getUser().getEmail());
        }
        model.addAttribute(ATTR_CHANGE_EMAIL_FORM, form);
        model.addAttribute(ATTR_ACTION, "change-email");
        return showProfile(
                showProfileService.getSecurity(id),
                ProfileSection.SECURITY,
                model
        );
    }

    @PostMapping(USER_PROFILE_CHANGE_EMAIL)
    public String checkChangeEmailForm(@Valid ChangeEmailForm form, BindingResult result, @PathVariable int id, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Check change email request: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        try {
            if(!result.hasErrors()) {
                editProfileService.changeEmail(form);
            }
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
            return showChangeEmailForm(UserUtils.getCurrentUser().getId(), form, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_EMAIL, id);
    }

    @GetMapping(USER_PROFILE_SAVE_EMAIL)
    public String saveChangeEmailForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SECURITY, id);
    }

    @GetMapping(USER_PROFILE_CHANGE_PASSWORD)
    public String showChangePasswordForm(@PathVariable int id, ChangePasswordForm form, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Change password form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        model.addAttribute(ATTR_CHANGE_PASSWORD_FORM, form);
        model.addAttribute(ATTR_ACTION, "change-password");
        return showProfile(
                showProfileService.getSecurity(id),
                ProfileSection.SECURITY,
                model
        );
    }

    @PostMapping(USER_PROFILE_CHANGE_PASSWORD)
    public String checkChangePasswordForm(@Valid ChangePasswordForm form, BindingResult result, @PathVariable int id,  Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Check change password form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        try {
            if(!result.hasErrors()) {
                editProfileService.changePassword(form);
            }
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
        }
        if(result.hasErrors()) {
            return showChangePasswordForm(UserUtils.getCurrentUser().getId(), form, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_PASSWORD, id);
    }

    @GetMapping(USER_PROFILE_SAVE_PASSWORD)
    public String saveChangePasswordForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SECURITY, id);
    }

    @GetMapping(USER_PROFILE_CHANGE_SECRET_ANSWER)
    public String showChangeSecretAnswerForm(@PathVariable int id, ChangeSecretAnswerForm form, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Change secret answer form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        model.addAttribute(ATTR_CHANGE_SECRET_ANSWER_FORM, form);
        model.addAttribute(ATTR_ACTION, "change-secret-answer");
        return showProfile(
                showProfileService.getSecurity(id),
                ProfileSection.SECURITY,
                model
        );
    }

    @PostMapping(USER_PROFILE_CHANGE_SECRET_ANSWER)
    public String checkChangeSecretAnswerForm(@Valid ChangeSecretAnswerForm form, BindingResult result, @PathVariable int id, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Check change secret answer form: requested account id '{}' does not match current user account id '{}'", id, UserUtils
                    .getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        try {
            if(!result.hasErrors()) {
                editProfileService.changeSecretAnswer(form);
            }
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
        }
        if(result.hasErrors()) {
            return showChangeSecretAnswerForm(UserUtils.getCurrentUser().getId(), form, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_SECRET_ANSWER, id);
    }

    @GetMapping(USER_PROFILE_SAVE_SECRET_ANSWER)
    public String saveChangeSecretAnswerForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SECURITY, id);
    }

    @GetMapping(USER_PROFILE_EDIT_OVERVIEW)
    public String showEditOverviewForm(@PathVariable int id, EditOverviewForm form, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Edit overview form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        if(form != null && form.getName() == null) {
            form = profileMapper.userToEditOverviewForm(UserUtils.getCurrentUser().getUser());
        }
        model.addAttribute(ATTR_EDIT_OVERVIEW_FORM, form);
        model.addAttribute(ATTR_GENDER_LIST, Gender.getValues());
        model.addAttribute(ATTR_ACTIVE_LIST, Utils.getActiveValues(recordService));
        model.addAttribute(ATTR_ACTION, "edit");
        return showProfile(
                showProfileService.getOverview(id),
                ProfileSection.OVERVIEW,
                model
        );
    }

    @PostMapping(USER_PROFILE_EDIT_OVERVIEW)
    public String checkEditOverviewForm(@Valid EditOverviewForm form, BindingResult result, @PathVariable int id, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Check edit overview form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        try {
            if(!result.hasErrors()) {
                editProfileService.editOverview(form);
            }
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
        }
        if(result.hasErrors()) {
            return showEditOverviewForm(UserUtils.getCurrentUser().getId(), form, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_OVERVIEW, id);
    }

    @GetMapping(USER_PROFILE_SAVE_OVERVIEW)
    public String saveEditOverviewForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_OVERVIEW, id);
    }

    @GetMapping(USER_PROFILE_EDIT_AVATAR)
    public String showEditAvatarForm(@PathVariable int id, EditAvatarForm editAvatarForm, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Edit avatar form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        model.addAttribute(ATTR_EDIT_AVATAR_FORM, editAvatarForm);
        model.addAttribute(ATTR_ACTION, "edit-avatar");
        return showProfile(
                showProfileService.getOverview(id),
                ProfileSection.OVERVIEW,
                model
        );
    }

    @PostMapping(USER_PROFILE_EDIT_AVATAR)
    public String checkEditAvatarForm(@Valid EditAvatarForm editAvatarForm, BindingResult result, @PathVariable int id, Model model) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Check edit avatar form: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        try {
            if(!result.hasErrors()) {
                editProfileService.editAvatar(editAvatarForm);
            }
        }
        catch(UserProfileException e) {
            addErrors(result, recordService, e.getErrors());
        }
        if(result.hasErrors()) {
            return showEditAvatarForm(UserUtils.getCurrentUser().getId(), editAvatarForm, model);
        }
        // PRG pattern
        return "redirect:" + buildSegmentsPath(USER_PROFILE_SAVE_AVATAR, id);
    }

    @GetMapping(USER_PROFILE_SAVE_AVATAR)
    public String saveEditAvatarForm(@PathVariable int id) {
        return "redirect:" + buildSegmentsPath(USER_PROFILE_OVERVIEW, id);
    }


    @GetMapping(USER_PROFILE_REMOVE_AVATAR)
    public String removeAvatar(@PathVariable int id) {
        if(id != UserUtils.getCurrentUser().getId()) {
            log.error("Remove avatar: requested account id '{}' does not match current user account id '{}'", id, UserUtils.getCurrentUser().getUser().getId());
            throw new ForbiddenException();
        }
        editProfileService.removeAvatar();
        return "redirect:" + buildSegmentsPath(USER_PROFILE_EDIT_AVATAR, id);
    }
}
