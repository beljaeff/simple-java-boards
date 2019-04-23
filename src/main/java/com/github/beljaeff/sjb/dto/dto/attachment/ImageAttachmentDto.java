package com.github.beljaeff.sjb.dto.dto.attachment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageAttachmentDto extends AttachmentDto {
    private String imagePath;
    private String imageUrl;
    private String previewPath;
    private String previewUrl;
    private String smallPreviewPath;
    private String smallPreviewUrl;
}