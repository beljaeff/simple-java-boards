package com.github.beljaeff.sjb.service.attachment;

import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.exception.AttachmentException;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
class FileAttachmentServiceImpl implements FileAttachmentService {
    @Value("${files.upload.storage.path}")
    private String uploadPath;

    @Value("${uri.upload.prefix}")
    private String uploadPrefix;

    @Value("${posts.folder}")
    private String postsFolder;

    @Value("${pm.folder}")
    private String pmFolder;

    @Value("${avatars.folder}")
    private String avatarsFolder;

    private ThumbnailsService thumbnailsService;

    @Autowired
    public FileAttachmentServiceImpl(ThumbnailsService thumbnailsService) {
        this.thumbnailsService = thumbnailsService;
    }

    private String getUploadPath(AttachmentType type) {
        if(type == null) {
            log.error("Can not set upload path because attachment type not set");
            throw new AttachmentException();
        }

        if(StringUtils.isEmpty(uploadPath)) {
            log.error("Attachment upload path is empty");
            throw new AttachmentException();
        }

        String path = new StringBuilder(uploadPath)
                .append(uploadPath.endsWith(File.separator) ? "" : File.separator)
                .append(type.getType())
                .append(File.separator)
                .toString();

        Path p = Paths.get(path);
        if(Files.notExists(p) || !Files.isDirectory(p) || !Files.isReadable(p) || !Files.isWritable(p)) {
            log.error("Attachment upload path not exists, not a directory or not readable/writable: '{}'", path);
            throw new AttachmentException();
        }

        return path;
    }

    private String getExtension(String fileName) {
        if(StringUtils.isEmpty(fileName)) {
            log.debug("Filename is empty, can not determine extension");
            return "";
        }

        int lastIndexOf = fileName.lastIndexOf(".");
        if(lastIndexOf == -1) {
            log.debug("Filename has no extension");
            return "";
        }

        return fileName.substring(lastIndexOf);
    }

    private String calculatePath(Attachment attachment, String basePath) {
        if(attachment == null || StringUtils.isEmpty(basePath)) {
            log.error("Attachment or upload path is not set. Attachment: {}, upload path: {}", attachment, basePath);
            throw new AttachmentException();
        }

        return new StringBuilder(basePath)
                .append(uploadPrefix.endsWith(File.separator) ? "" : File.separator)
                .append(attachment.getDateUpload().getYear())
                .append(File.separator)
                .append(attachment.getDateUpload().format(DateTimeFormatter.ofPattern("MMdd")))
                .append(File.separator)
                .append(attachment.getFileName())
                .toString();
    }

    @Override
    public List<Attachment> createAttachments(MultipartFile[] files, AttachmentType type) {
        List<Attachment> attachments = new ArrayList<>();
        for(MultipartFile file : files) {
            Attachment attachment = createAttachment(file, type);
            if (attachment == null) {
                log.warn("Can not create attachment for file: {}", file);
                continue;
            }
            attachments.add(attachment);
        }
        return attachments;
    }

    @Override
    public Attachment createAttachment(MultipartFile file, AttachmentType type) {
        if(file == null || file.isEmpty()) {
            log.warn("Uploaded file is null or empty: {}", file);
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + getExtension(originalName);

        Attachment attachment = new Attachment();
        attachment.setType(type);
        attachment.setContentType(file.getContentType());
        attachment.setDateUpload(now);
        attachment.setOriginalName(originalName);
        attachment.setFileName(fileName);

        String filePath = calculatePath(attachment, getUploadPath(type));
        Path directoryPath = Paths.get(filePath).getParent();
        if(!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            }
            catch(IOException ioe) {
                log.warn("Can not create folders for following path '{}' to copy uploaded file {}", directoryPath, file);
                log.warn(ioe.getMessage(), ioe);
                return null;
            }
        }
        try {
            file.transferTo(Paths.get(filePath));
        }
        catch(IOException ioe) {
            log.warn("Can not transfer uploaded file {} to following path '{}'", file, filePath);
            log.warn(ioe.getMessage(), ioe);
            return null;
        }

        if(Utils.isImage(attachment.getContentType())) {
            thumbnailsService.createThumbnails(directoryPath.toString(), attachment);
        }

        return attachment;
    }

    @Override
    public void delete(Attachment attachment) {
        if(attachment == null) {
            log.warn("Attachment is empty, can not perform removing");
            return;
        }
        String uploadFolderPath = getUploadPath(attachment.getType());
        String attachmentFilePath = calculatePath(attachment, uploadFolderPath);
        Path p = Paths.get(attachmentFilePath);
        if(Files.exists(p) && !Files.isDirectory(p) && Files.isReadable(p) && Files.isWritable(p)) {
            try {
                Files.delete(p);
                thumbnailsService.removeThumbnails(Paths.get(attachmentFilePath).getParent().toString(), attachment);
            }
            catch (IOException ioe) {
                log.warn("Can not delete attachment {} from following path '{}'", attachment, p);
                log.warn(ioe.getMessage(), ioe);
            }
        }
        else {
            log.warn("Can not delete attachment {}. File not exists, given path is directory, or file is not readable/writable", attachment, p);
        }
    }

    private String getWebUploadPath(AttachmentType type) {
        return new StringBuilder(uploadPrefix)
                .append(uploadPrefix.endsWith(File.separator) ? "" : File.separator)
                .append(type.getType())
                .append(File.separator)
                .toString();
    }

    @Override
    public AttachmentPathsDto getAttachmentPaths(Attachment attachment) {
        Assert.notNull(attachment,"attachment is null");

        String filePath = calculatePath(attachment, getUploadPath(attachment.getType()));
        String url = calculatePath(attachment, getWebUploadPath(attachment.getType()));
        Path directoryPath = Paths.get(filePath).getParent();
        Path directoryUrl = Paths.get(url).getParent();

        AttachmentPathsDto ret = new AttachmentPathsDto();
        ret.setFilePath(filePath);
        ret.setFileUrl(url);
        if(Utils.isImage(attachment.getContentType())) {
            AttachmentPathsDto prefixes = thumbnailsService.getPreviewPrefixesByAttachment(attachment);
            if(prefixes.getImagePath() != null) {
                ret.setImagePath(Paths.get(directoryPath.toString(), prefixes.getImagePath() + attachment.getFileName()).toString());
                ret.setImageUrl(Paths.get(directoryUrl.toString(), prefixes.getImagePath() + attachment.getFileName()).toString());
            }
            if(prefixes.getPreviewPath() != null) {
                ret.setPreviewPath(Paths.get(directoryPath.toString(), prefixes.getPreviewPath() + attachment.getFileName()).toString());
                ret.setPreviewUrl(Paths.get(directoryUrl.toString(), prefixes.getPreviewPath() + attachment.getFileName()).toString());
            }
            if(prefixes.getSmallPreviewPath() != null) {
                ret.setSmallPreviewPath(Paths.get(directoryPath.toString(), prefixes.getSmallPreviewPath() + attachment.getFileName()).toString());
                ret.setSmallPreviewUrl(Paths.get(directoryUrl.toString(), prefixes.getSmallPreviewPath() + attachment.getFileName()).toString());
            }
        }
        return ret;
    }
}