package com.github.beljaeff.sjb.dto.form.profile;

import com.github.beljaeff.sjb.dto.form.security.AbstractDoublePasswordForm;
import com.github.beljaeff.sjb.validator.PasswordsEqual;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@PasswordsEqual(message = "{passwords.not.equal}")
public class ChangePasswordForm extends AbstractDoublePasswordForm {
    @NotBlank(message = "{profile.form.current.password.empty}")
    private String currentPassword;
}