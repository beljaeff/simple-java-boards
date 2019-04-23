package com.github.beljaeff.sjb.validator;

import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryForBoardCheckValidator implements ConstraintValidator<CategoryForBoardCheck, BoardForm> {

    @Override
    public void initialize(CategoryForBoardCheck constraintAnnotation) {}

    @Override
    public boolean isValid(BoardForm form, ConstraintValidatorContext context) {
        // Categories available only for top-level board
        return !(form.getParentBoard() != null && form.getCategory() != null);
    }
}
