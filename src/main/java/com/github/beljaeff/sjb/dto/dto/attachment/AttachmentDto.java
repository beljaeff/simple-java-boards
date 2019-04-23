package com.github.beljaeff.sjb.dto.dto.attachment;

import com.github.beljaeff.sjb.enums.AttachmentType;
import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;

@Getter
@Setter
public class AttachmentDto extends BaseDto {
    private boolean isImage;
    private String filePath;
    private String fileUrl;
    private String originalName;
    private String dateUpload;
    private String description;
    private AttachmentType type;
}
