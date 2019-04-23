package com.github.beljaeff.sjb.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryForBoardCheckValidator.class)
public @interface CategoryForBoardCheck {
    String message() default "only top level board can have category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}