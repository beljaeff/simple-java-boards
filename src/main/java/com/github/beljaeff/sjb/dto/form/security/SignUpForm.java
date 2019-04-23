package com.github.beljaeff.sjb.dto.form.security;

import com.github.beljaeff.sjb.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpForm extends AbstractDoublePasswordForm {

    @NotBlank(message = "{sign.up.form.name.empty}")
    @Size(min = 2, max = 64, message = "{sign.up.form.name.size.incorrect}")
    private String name;

    @Size(max = 64, message = "{sign.up.form.surname.size.incorrect}")
    private String surname;

    private Gender gender;

    @NotBlank(message = "{sign.up.form.email.empty}")
    @Email(message = "{sign.up.form.email.incorrect}", regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Size(max = 64, message = "{sign.up.form.email.size.incorrect}")
    private String email;

    @NotBlank(message = "{sign.up.form.nick.name.empty}")
    @Size(min = 3, max = 64, message = "{sign.up.form.nick.name.size.incorrect}")
    @Pattern(regexp = "^[_a-zA-Z0-9-.]{3,}$", message = "{sign.up.form.nick.name.incorrect}")
    private String nickName;
}