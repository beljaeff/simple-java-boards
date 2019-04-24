package com.github.beljaeff.sjb.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.github.beljaeff.sjb.enums.BasePermission.ADMIN;
import static com.github.beljaeff.sjb.enums.BasePermission.CREATE_ATTACHMENTS;
import static com.github.beljaeff.sjb.util.UserUtils.hasPermission;

public class PostUploadPermissionValidator implements ConstraintValidator<PostUploadPermission, MultipartFile[]> {

    @Override
    public boolean isValid(MultipartFile[] uploads, ConstraintValidatorContext context) {
        return uploads == null || uploads.length == 0 || hasPermission(CREATE_ATTACHMENTS) || hasPermission(ADMIN);
    }
}
