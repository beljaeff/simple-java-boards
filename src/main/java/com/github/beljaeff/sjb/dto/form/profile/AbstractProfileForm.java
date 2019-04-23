package com.github.beljaeff.sjb.dto.form.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
abstract public class AbstractProfileForm {
    @PositiveOrZero(message = "{profile.form.user.incorrect}")
    private int idUser;
}
