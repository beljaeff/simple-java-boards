package com.github.beljaeff.sjb.dto.form.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordForm extends AbstractDoublePasswordForm {

    @NotBlank(message = "{reset.password.form.validation.code.empty}")
    private String validationCode;
}