package com.github.beljaeff.sjb.mapper;

import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.dto.dto.attachment.ImageAttachmentDto;
import com.github.beljaeff.sjb.model.Attachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.github.beljaeff.sjb.util.Utils.isImage;

@Component
public class AttachmentMapper extends AbstractMapper {

    private CommonAttachmentService attachmentService;

    @Autowired
    public void setAttachmentService(CommonAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public List<AttachmentDto> attachmentToAttachmentDto(List<Attachment> attachments) {
        if(!isLoaded(attachments) || CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyList();
        }
        List<AttachmentDto> ret = new ArrayList<>();
        for(Attachment attachment : attachments) {
            if(attachment != null) {
                ret.add(attachmentToAttachmentDto(attachment));
            }
        }
        ret.sort(Comparator.comparing(AttachmentDto::getIsImage).reversed());
        return ret;
    }

    public AttachmentDto attachmentToAttachmentDto(Attachment attachment) {
        if(!isLoaded(attachment) || attachment == null) {
            return null;
        }

        AttachmentPathsDto paths = attachmentService.getPaths(attachment);
        AttachmentDto dto = new AttachmentDto();

        if(isImage(attachment.getContentType())) {
            ImageAttachmentDto iDto = new ImageAttachmentDto();
            iDto.setIsImage(true);
            iDto.setImagePath(paths.getImagePath());
            iDto.setImageUrl(paths.getImageUrl());
            iDto.setPreviewPath(paths.getPreviewPath());
            iDto.setPreviewUrl(paths.getPreviewUrl());
            iDto.setSmallPreviewPath(paths.getSmallPreviewPath());
            iDto.setSmallPreviewUrl(paths.getSmallPreviewUrl());
            dto = iDto;
        }
        dto.setFilePath(paths.getFilePath());
        dto.setFileUrl(paths.getFileUrl());

        dto.setId(attachment.getId());
        dto.setIsActive(attachment.getIsActive());
        dto.setOriginalName(attachment.getOriginalName());
        dto.setDateUpload(dateTimeToString(attachment.getDateUpload()));
        dto.setDescription(attachment.getDescription());
        dto.setType(attachment.getType());

        return dto;
    }
}
