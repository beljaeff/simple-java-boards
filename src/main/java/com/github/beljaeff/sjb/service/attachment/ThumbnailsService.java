package com.github.beljaeff.sjb.service.attachment;

import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.model.Attachment;

interface ThumbnailsService {
    void createThumbnails(String folder, Attachment attachment);
    void removeThumbnails(String folder, Attachment attachment);

    AttachmentPathsDto getPreviewPrefixesByAttachment(Attachment attachment);
}
