package com.github.beljaeff.sjb.dto.form.conversation;

import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.validator.PositionCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryForm extends BaseForm {
    @PositiveOrZero(message = "{category.form.incorrect.id}")
    private int id;

    @NotBlank(message = "{category.form.title.empty}")
    @Size(min = 2, max = 128, message = "{category.form.title.size.incorrect}")
    private String title;

    private Boolean isActive;

    @PositionCheck(message = "{category.form.position.incorrect}")
    private String position;
}
