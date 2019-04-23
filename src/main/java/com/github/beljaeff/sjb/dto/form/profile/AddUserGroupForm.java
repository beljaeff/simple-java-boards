package com.github.beljaeff.sjb.dto.form.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class AddUserGroupForm extends AbstractProfileForm {
    @PositiveOrZero(message = "{add.user.group.form.group.incorrect}")
    private int idGroup;
}
