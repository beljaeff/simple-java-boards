package com.github.beljaeff.sjb.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PostUploadPermissionValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostUploadPermission {
    String message() default "insufficient permissions to upload files";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}