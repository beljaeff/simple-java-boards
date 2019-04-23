package com.github.beljaeff.sjb.dto.form.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangeEmailForm extends AbstractPasswordForm {
    @NotBlank(message = "{change.email.form.email.empty}")
    @Email(message = "{change.email.form.email.incorrect}", regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Size(max = 64, message = "{change.email.form.email.size.incorrect}")
    private String email;
}