package com.github.beljaeff.sjb.dto.form.profile;

import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.validator.Image;
import com.github.beljaeff.sjb.validator.UploadNotEmpty;
import com.github.beljaeff.sjb.validator.UploadedFileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EditAvatarForm {
    @UploadNotEmpty(message = "{edit.avatar.form.avatar.not.set}")
    @Image(message = "{edit.avatar.form.avatar.not.image}")
    @UploadedFileSize(message = "{edit.avatar.form.avatar.file.size.incorrect}", type = AttachmentType.AVATAR)
    private MultipartFile avatar;
}
