package com.github.beljaeff.sjb.service.profile;

import com.github.beljaeff.sjb.exception.UserProfileException;
import com.github.beljaeff.sjb.repository.condition.UserCondition;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import com.github.beljaeff.sjb.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.dto.form.profile.ChangeEmailForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangePasswordForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangeSecretAnswerForm;
import com.github.beljaeff.sjb.dto.form.profile.EditAvatarForm;
import com.github.beljaeff.sjb.dto.form.profile.EditOverviewForm;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.mapper.ProfileMapper;

import java.util.Collections;

import static com.github.beljaeff.sjb.enums.ErrorCode.EMAIL_DUPLICATE;
import static com.github.beljaeff.sjb.enums.ErrorCode.PASSWORD_NOT_MATCH;
import static com.github.beljaeff.sjb.util.UserUtils.getCurrentUser;

@Slf4j
@Service
public class EditOwnProfileServiceImpl implements EditOwnProfileService {

    private final UserService userService;
    private final CommonAttachmentService commonAttachmentService;

    private final ProfileMapper profileMapper;

    @Autowired
    public EditOwnProfileServiceImpl(UserService userService, CommonAttachmentService commonAttachmentService, ProfileMapper profileMapper) {
        this.userService = userService;
        this.commonAttachmentService = commonAttachmentService;
        this.profileMapper = profileMapper;
    }

    private User checkPasswordAndGet(String currentPassword) {
        User userEntity = userService.get(getCurrentUser().getId());
        if(!userService.passwordsMatch(userEntity.getPassword(), currentPassword)) {
            log.warn("Password does not match");
            throw new UserProfileException(Collections.singletonList(PASSWORD_NOT_MATCH));
        }
        return userEntity;
    }

    @Override
    @Transactional
    public void changeEmail(ChangeEmailForm form) {
        Assert.notNull(form, "change email form should be set");

        UserCondition condition = new UserCondition();
        condition.setEmail(form.getEmail());
        User userCheck = userService.getByCondition(condition);
        if(userCheck != null) {
            log.warn("Email duplication detected for '{}'", form);
            throw new UserProfileException(Collections.singletonList(EMAIL_DUPLICATE));
        }

        User userEntity = checkPasswordAndGet(form.getCurrentPassword());
        userEntity.setEmail(form.getEmail());

        userService.save(userEntity);

        getCurrentUser().getUser().setEmail(form.getEmail());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordForm form) {
        Assert.notNull(form, "change password form should be set");

        User userEntity = checkPasswordAndGet(form.getCurrentPassword());
        userEntity.setPassword(userService.encodePassword(form.getPassword()));

        userService.save(userEntity);
    }

    @Override
    public void changeSecretAnswer(ChangeSecretAnswerForm form) {
        Assert.notNull(form, "change secret answer form should be set");

        User userEntity = checkPasswordAndGet(form.getCurrentPassword());
        userEntity.setSecretQuestion(form.getSecretQuestion());
        userEntity.setSecretAnswer(form.getSecretAnswer());

        userService.save(userEntity);

        getCurrentUser().getUser().setSecretQuestion(form.getSecretQuestion());
        getCurrentUser().getUser().setSecretAnswer(form.getSecretAnswer());
    }

    @Override
    public void editOverview(EditOverviewForm form) {
        Assert.notNull(form, "edit overview form should be set");

        User userEntity = userService.get(getCurrentUser().getId());
        profileMapper.updateUserFromForm(userEntity, form);

        userService.save(userEntity);

        profileMapper.updateUserFromForm(getCurrentUser().getUser(), form);
    }

    @Override
    @Transactional
    public void editAvatar(EditAvatarForm form) {
        Assert.notNull(form, "edit avatar form should be set");

        removeAvatar();

        User userEntity = userService.get(getCurrentUser().getId());

        Attachment avatar = commonAttachmentService.createAvatar(form.getAvatar());
        userEntity.setAvatar(avatar);

        userService.save(userEntity);
        getCurrentUser().getUser().setAvatar(avatar);
    }

    @Override
    @Transactional
    public void removeAvatar() {
        User user = userService.getWithGraph(getCurrentUser().getId(), EntityGraphNamesHelper.USERS_WITH_AVATAR);
        Attachment avatar = user.getAvatar();
        if(avatar != null) {
            user.setAvatar(null);
            userService.save(user);
            commonAttachmentService.delete(avatar.getId());
            getCurrentUser().getUser().setAvatar(null);
        }
    }
}
