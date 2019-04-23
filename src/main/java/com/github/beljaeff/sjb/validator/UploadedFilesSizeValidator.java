package com.github.beljaeff.sjb.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import com.github.beljaeff.sjb.enums.AttachmentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

import static com.github.beljaeff.sjb.enums.AttachmentType.AVATAR;
import static com.github.beljaeff.sjb.enums.AttachmentType.POST;
import static com.github.beljaeff.sjb.enums.BasePermission.ADMIN;
import static com.github.beljaeff.sjb.enums.BasePermission.CREATE_ATTACHMENTS;
import static com.github.beljaeff.sjb.util.UserUtils.hasPermission;

public class UploadedFilesSizeValidator implements ConstraintValidator<UploadedFileSize, MultipartFile[]> {

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
    public boolean isValid(MultipartFile[] uploads, ConstraintValidatorContext context) {
        if(uploads == null || uploads.length == 0 || !hasPermission(CREATE_ATTACHMENTS) || !hasPermission(ADMIN)) {
            return true;
        }
        int maxSize = type == AVATAR ? maxAvatarSize : type == POST ? maxPostSize : maxPmSize;
        List<String> errorFiles = new ArrayList<>();
        for(MultipartFile upload : uploads) {
            if(upload == null || upload.isEmpty() || upload.getSize() > maxSize) {
                errorFiles.add(upload == null ? "" : upload.getOriginalFilename());
            }
        }
        return errorFiles.size() == 0;
    }
}
