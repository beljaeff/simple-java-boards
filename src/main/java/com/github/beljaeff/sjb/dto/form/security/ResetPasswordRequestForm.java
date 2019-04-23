package com.github.beljaeff.sjb.dto.form.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordRequestForm {

    @Size(min = 3, max = 64, message = "{reset.password.request.form.input.size.incorrect}")
    @Pattern(regexp = "^[@_a-zA-Z0-9-.]{3,}$", message = "{reset.password.request.form.input.incorrect}")
    private String inputId;
}