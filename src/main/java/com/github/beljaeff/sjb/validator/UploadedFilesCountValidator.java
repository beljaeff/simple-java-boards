package com.github.beljaeff.sjb.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UploadedFilesCountValidator implements ConstraintValidator<UploadedFilesCount, MultipartFile[]> {

    private static final int MAX_UPLOADED_FILES = 4;

    @Value("${post.max.attachments.count.per.post}")
    private int maxUploadedFiles;

    @Override
    public void initialize(UploadedFilesCount constraintAnnotation) {
        if(constraintAnnotation.count() >= 0) {
            this.maxUploadedFiles = constraintAnnotation.count();
        }
        if(maxUploadedFiles < 0) {
            maxUploadedFiles = MAX_UPLOADED_FILES;
        }
    }

    @Override
    public boolean isValid(MultipartFile[] files, ConstraintValidatorContext context) {
        return files == null || files.length <= maxUploadedFiles;
    }
}
