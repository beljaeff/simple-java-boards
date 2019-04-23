package com.github.beljaeff.sjb.dto.form.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangeSecretAnswerForm extends AbstractPasswordForm {
    @NotBlank(message = "{change.secret.answer.form.question.empty}")
    private String secretQuestion;

    @NotBlank(message = "{change.secret.answer.form.answer.empty}")
    private String secretAnswer;
}