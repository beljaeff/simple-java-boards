package com.github.beljaeff.sjb.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UploadedFilesCountValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadedFilesCount {
    String message() default "attachment count too large";

    int count() default -1;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}