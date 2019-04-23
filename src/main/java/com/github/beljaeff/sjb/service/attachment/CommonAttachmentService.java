package com.github.beljaeff.sjb.service.attachment;

import org.springframework.web.multipart.MultipartFile;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;

import java.util.List;

public interface CommonAttachmentService {
    AttachmentDto get(int id);

    AttachmentPathsDto getPaths(Attachment attachment);

    ActionStatusDto<Attachment> delete(int id);

    List<Attachment> createPostAttachments(MultipartFile[] files);

    List<Attachment> mergePostAttachments(final MultipartFile[] uploads, final List<Attachment> attachments);

    void removeAttachments(Category category);

    void removeAttachments(Board board);

    void removeAttachments(Topic topic);

    void removeAttachments(Post post);

    Attachment createAvatar(MultipartFile avatar);
}
