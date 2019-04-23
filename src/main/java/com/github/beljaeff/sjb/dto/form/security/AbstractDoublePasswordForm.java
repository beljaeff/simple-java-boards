package com.github.beljaeff.sjb.dto.form.security;

import com.github.beljaeff.sjb.validator.PasswordStrength;
import com.github.beljaeff.sjb.validator.PasswordsEqual;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordsEqual(message = "{passwords.not.equal}")
public abstract class AbstractDoublePasswordForm {

    @NotBlank(message = "{reset.password.form.password.empty}")
    @Size(min = 5, max = 64, message = "{reset.password.form.password.size.incorrect}")
    @PasswordStrength(message = "{reset.password.form.password.too.weak}")
    private String password;

    @NotBlank(message = "{reset.password.form.password.confirm.empty}")
    @Size(min = 5, max = 64, message = "{reset.password.form.password.confirm.size.incorrect}")
    private String passwordConfirm;
}
