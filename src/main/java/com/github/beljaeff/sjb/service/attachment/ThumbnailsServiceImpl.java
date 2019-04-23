package com.github.beljaeff.sjb.service.attachment;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.model.Attachment;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
class ThumbnailsServiceImpl implements ThumbnailsService {
    @Value("${avatar.prefix}")
    private String avatarPrefix;

    @Value("${avatar.preview.prefix}")
    private String avatarPreviewPrefix;

    @Value("${avatars.preview.small.prefix}")
    private String avatarPreviewSmallPrefix;

    @Value("${post.prefix}")
    private String postPrefix;

    @Value("${post.preview.prefix}")
    private String postPreviewPrefix;

    @Value("${pm.prefix}")
    private String pmPrefix;

    @Value("${pm.preview.prefix}")
    private String pmPreviewPrefix;

    @Value("${avatar.preview.small.width}")
    private int avatarPreviewSmallWidth;

    @Value("${avatar.preview.small.height}")
    private int avatarPreviewSmallHeight;

    @Value("${avatar.preview.width}")
    private int avatarPreviewWidth;

    @Value("${avatar.preview.height}")
    private int avatarPreviewHeight;

    @Value("${avatar.width}")
    private int avatarWidth;

    @Value("${avatar.height}")
    private int avatarHeight;

    @Value("${post.image.preview.width}")
    private int postImagePreviewWidth;

    @Value("${post.image.preview.height}")
    private int postImagePreviewHeight;

    @Value("${post.image.width}")
    private int postImageWidth;

    @Value("${post.image.height}")
    private int postImageHeight;

    @Value("${pm.image.preview.width}")
    private int pmImagePreviewWidth;

    @Value("${pm.image.preview.height}")
    private int pmImagePreviewHeight;

    @Value("${pm.image.width}")
    private int pmImageWidth;

    @Value("${pm.image.height}")
    private int pmImageHeight;

    private Map<String, Dimension> getDimensionsAndPrefix(AttachmentType type) {
        Map<String, Dimension> ret = new HashMap<>();
        switch (type) {
            case POST:
                ret.put(postPreviewPrefix, new Dimension(postImagePreviewWidth, postImagePreviewHeight));
                ret.put(postPrefix, new Dimension(postImageWidth, postImageHeight));
                break;

            case AVATAR:
                ret.put(avatarPreviewPrefix, new Dimension(avatarPreviewWidth, avatarPreviewHeight));
                ret.put(avatarPreviewSmallPrefix, new Dimension(avatarPreviewSmallWidth, avatarPreviewSmallHeight));
                ret.put(avatarPrefix, new Dimension(avatarWidth, avatarHeight));
                break;

            case PRIVATE_MESSAGE:
                ret.put(pmPreviewPrefix, new Dimension(pmImagePreviewWidth, pmImagePreviewHeight));
                ret.put(pmPrefix, new Dimension(pmImageWidth, pmImageHeight));
                break;
        }
        return ret;
    }

    @Override
    public void createThumbnails(String folder, Attachment attachment) {
        Map<String, Dimension> dimensions = getDimensionsAndPrefix(attachment.getType());
        for(Map.Entry<String, Dimension> dimensionEntry : dimensions.entrySet()) {
            String prefix = dimensionEntry.getKey();
            Dimension dimension = dimensionEntry.getValue();
            try {
                Thumbnails.of(Paths.get(folder, attachment.getFileName()).toString())
                        .size(dimension.width, dimension.height)
                        .toFile(Paths.get(folder, prefix + attachment.getFileName()).toString());
            }
            catch (IOException ioe) {
                log.debug("Unable to create preview [{}] for attachment {}", prefix, attachment);
                log.debug(ioe.getMessage(), ioe);
            }
        }
    }

    @Override
    public void removeThumbnails(String folder, Attachment attachment) {
        Map<String, Dimension> dimensions = getDimensionsAndPrefix(attachment.getType());
        for(Map.Entry<String, Dimension> dimensionEntry : dimensions.entrySet()) {
            String prefix = dimensionEntry.getKey();
            try {
                Files.delete(Paths.get(folder, prefix + attachment.getFileName()));
            }
            catch (IOException ioe) {
                log.debug("Unable to delete preview [{}] for attachment {}", prefix, attachment);
                log.debug(ioe.getMessage(), ioe);
            }
        }
    }

    @Override
    public AttachmentPathsDto getPreviewPrefixesByAttachment(Attachment attachment) {
        AttachmentPathsDto dto = new AttachmentPathsDto();
        dto.setFilePath("");
        switch (attachment.getType()) {
            case AVATAR:
                dto.setImagePath(avatarPrefix);
                dto.setPreviewPath(avatarPreviewPrefix);
                dto.setSmallPreviewPath(avatarPreviewSmallPrefix);
                break;

            case POST:
                dto.setImagePath(postPrefix);
                dto.setPreviewPath(postPreviewPrefix);
                break;

            case PRIVATE_MESSAGE:
                dto.setImagePath(pmPrefix);
                dto.setPreviewPath(pmPreviewPrefix);
                break;
        }
        return dto;
    }
}