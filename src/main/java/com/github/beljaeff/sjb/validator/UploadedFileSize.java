package com.github.beljaeff.sjb.validator;

import com.github.beljaeff.sjb.enums.AttachmentType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = { UploadedFileSizeValidator.class, UploadedFilesSizeValidator.class })
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadedFileSize {
    String message() default "attachment size too big or zero";

    AttachmentType type() default AttachmentType.POST;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
