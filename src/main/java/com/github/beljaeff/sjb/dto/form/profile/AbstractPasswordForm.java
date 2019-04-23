package com.github.beljaeff.sjb.dto.form.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
abstract public class AbstractPasswordForm {
    @NotBlank(message = "{profile.form.current.password.empty}")
    private String currentPassword;
}
