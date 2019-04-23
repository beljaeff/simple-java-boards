package com.github.beljaeff.sjb.validator;

import lombok.extern.slf4j.Slf4j;
import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;
import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.ForbiddenException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.github.beljaeff.sjb.util.UserUtils.getCurrentUser;
import static com.github.beljaeff.sjb.util.UserUtils.hasPermission;

@Slf4j
public class BoardToCategoryPermissionCheckValidator implements ConstraintValidator<BoardToCategoryPermissionCheck, BoardForm> {

    @Override
    public void initialize(BoardToCategoryPermissionCheck constraintAnnotation) {}

    @Override
    public boolean isValid(BoardForm form, ConstraintValidatorContext context) {
        // If user add board into category he should have permission to do it
        if(form.getId() == 0 && form.getCategory() != null &&
           !hasPermission(BasePermission.EDIT_CATEGORY) && !hasPermission(BasePermission.ADMIN)) {
            log.error("User '{}' have no permission to add board to category", getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }
        return true;
    }
}