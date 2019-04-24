package com.github.beljaeff.sjb.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UploadNotEmptyValidator implements ConstraintValidator<UploadNotEmpty, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return !(file == null || file.isEmpty() || file.getSize() <= 0);
    }
}