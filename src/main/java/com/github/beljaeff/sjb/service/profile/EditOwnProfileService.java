package com.github.beljaeff.sjb.service.profile;

import com.github.beljaeff.sjb.dto.form.profile.ChangeEmailForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangePasswordForm;
import com.github.beljaeff.sjb.dto.form.profile.ChangeSecretAnswerForm;
import com.github.beljaeff.sjb.dto.form.profile.EditAvatarForm;
import com.github.beljaeff.sjb.dto.form.profile.EditOverviewForm;

public interface EditOwnProfileService {
    void changeEmail(ChangeEmailForm form);

    void changePassword(ChangePasswordForm form);

    void changeSecretAnswer(ChangeSecretAnswerForm form);

    void editOverview(EditOverviewForm form);

    void editAvatar(EditAvatarForm form);

    void removeAvatar();
}