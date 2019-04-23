package com.github.beljaeff.sjb.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.github.beljaeff.sjb.enums.AttachmentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.github.beljaeff.sjb.enums.AttachmentType.AVATAR;
import static com.github.beljaeff.sjb.enums.AttachmentType.POST;

public class UploadedFileSizeValidator implements ConstraintValidator<UploadedFileSize, MultipartFile> {

    private AttachmentType type;

    @Value("${avatar.max.size}")
    private int maxAvatarSize;

    @Value("${post.max.size}")
    private int maxPostSize;

    @Value("${pm.max.size}")
    private int maxPmSize;

    @Override
    public void initialize(UploadedFileSize constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        int maxSize = type == AVATAR ? maxAvatarSize : type == POST ? maxPostSize : maxPmSize;
        return !(file == null || file.isEmpty() || file.getSize() > maxSize);
    }
}
