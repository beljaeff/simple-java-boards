package com.github.beljaeff.sjb.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositionCheckValidator implements ConstraintValidator<PositionCheck, String> {

    @Override
    public boolean isValid(String position, ConstraintValidatorContext context) {
        try {
            return position == null || Integer.parseInt(position) >= 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}