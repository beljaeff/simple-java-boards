package com.github.beljaeff.sjb.dto.form.conversation;

import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.validator.PostUploadPermission;
import com.github.beljaeff.sjb.validator.UploadedFileSize;
import com.github.beljaeff.sjb.validator.UploadedFilesCount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import com.github.beljaeff.sjb.dto.form.BaseForm;

@Getter
@Setter
abstract class AbstractAttachmentForm extends BaseForm {
    @PostUploadPermission(message = "{post.uploads.insufficient.permissions}")
    @UploadedFileSize(message = "{post.uploads.file.size.incorrect}", type = AttachmentType.POST)
    @UploadedFilesCount(message = "{post.form.too.many.attachments}")
    private MultipartFile[] attachments;
}
