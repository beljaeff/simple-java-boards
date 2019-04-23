package com.github.beljaeff.sjb.service.attachment;

import org.springframework.web.multipart.MultipartFile;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.model.Attachment;

import java.util.List;

interface FileAttachmentService {
    void delete(Attachment attachment);
    Attachment createAttachment(MultipartFile file, AttachmentType type);
    List<Attachment> createAttachments(MultipartFile[] files, AttachmentType type);

    AttachmentPathsDto getAttachmentPaths(Attachment attachment);
}
