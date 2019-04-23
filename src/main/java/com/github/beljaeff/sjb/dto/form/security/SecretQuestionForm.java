package com.github.beljaeff.sjb.dto.form.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SecretQuestionForm {

    @NotBlank(message = "{reset.password.secret.question.nick.name.empty}")
    @Size(min = 3, max = 64, message = "{reset.password.secret.question.nick.name.size.incorrect}")
    @Pattern(regexp = "^[_a-zA-Z0-9-.]{3,}$", message = "{reset.password.secret.question.nick.name.incorrect}")
    private String nickName;

    @NotBlank(message = "{reset.password.secret.question.answer.empty}")
    private String secretAnswer;
}
