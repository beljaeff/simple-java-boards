package com.github.beljaeff.sjb.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositionCheckValidator.class)
public @interface PositionCheck {
    String message() default "email not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}