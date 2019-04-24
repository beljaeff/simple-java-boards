package com.github.beljaeff.sjb.validator;

import com.github.beljaeff.sjb.dto.form.security.AbstractDoublePasswordForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsEqualValidator implements ConstraintValidator<PasswordsEqual, AbstractDoublePasswordForm> {

    @Override
    public boolean isValid(AbstractDoublePasswordForm form, ConstraintValidatorContext constraintValidatorContext) {
        return form != null &&
               form.getPassword() != null &&
               form.getPasswordConfirm() != null &&
               form.getPassword().equals(form.getPasswordConfirm());
    }
}