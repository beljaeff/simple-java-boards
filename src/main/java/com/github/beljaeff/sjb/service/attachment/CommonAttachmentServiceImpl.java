package com.github.beljaeff.sjb.service.attachment;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentPathsDto;
import com.github.beljaeff.sjb.enums.AttachmentType;
import com.github.beljaeff.sjb.mapper.AttachmentMapper;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class CommonAttachmentServiceImpl implements CommonAttachmentService {

    @Value("${post.max.attachments.count.per.post}")
    private int maxAttachments;

    private final FileAttachmentService fileAttachmentService;
    private final DbAttachmentService dbAttachmentService;

    private AttachmentMapper attachmentMapper;

    @Autowired
    public CommonAttachmentServiceImpl(FileAttachmentService fileAttachmentService, DbAttachmentService dbAttachmentService) {
        this.fileAttachmentService = fileAttachmentService;
        this.dbAttachmentService = dbAttachmentService;
    }

    @Autowired
    public void setAttachmentMapper(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public AttachmentDto get(int id) {
        return attachmentMapper.attachmentToAttachmentDto(dbAttachmentService.get(id));
    }

    @Override
    public AttachmentPathsDto getPaths(Attachment attachment) {
        return fileAttachmentService.getAttachmentPaths(attachment);
    }

    @Override
    public List<Attachment> createPostAttachments(MultipartFile[] files) {
        if(Utils.isAttachmentsEmpty(files)) {
            return Collections.emptyList();
        }
        return fileAttachmentService.createAttachments(files, AttachmentType.POST);
    }

    @Override
    public List<Attachment> mergePostAttachments(final MultipartFile[] uploads, final List<Attachment> existed) {
        List<Attachment> attachments = existed == null ? new ArrayList<>() : existed;

        if(!Utils.isAttachmentsEmpty(uploads)) {
            // Checks uploaded attachments - total attachments count for post have to be less than maxAttachments constraint.
            // It means that if maxAttachments == 5, post already have 3 attachments, so max 2 uploads can be added to this post.
            // After that remove attachments.
            int difference = maxAttachments - attachments.size();
            if(difference > 0 && uploads.length > difference) {
                for(int i = difference; i < uploads.length; i++) {
                    uploads[i] = null;
                }
            }

            List<Attachment> newAttachments = createPostAttachments(uploads);
            if(!isEmpty(newAttachments)) {
                attachments.addAll(newAttachments);
            }
        }

        return attachments;
    }

    @Override
    public ActionStatusDto<Attachment> delete(int id) {
        ActionStatusDto<Attachment> entity = dbAttachmentService.delete(id);
        fileAttachmentService.delete(entity.getEntity());
        return entity;
    }

    @Override
    public Attachment createAvatar(MultipartFile avatar) {
        return fileAttachmentService.createAttachment(avatar, AttachmentType.AVATAR);
    }

    @Override
    public void removeAttachments(Category category) {
        if(category == null) {
            log.debug("Trying remove attachments for null category");
            return;
        }
        List<Board> boards = category.getBoards();
        if(boards != null) {
            for(Board board : boards) {
                removeAttachments(board);
            }
        }
    }

    @Override
    public void removeAttachments(Board board) {
        if(board == null) {
            log.debug("Trying remove attachments for null board");
            return;
        }
        List<Board> childBoards = board.getChildBoards();
        if(childBoards != null) {
            for(Board b : childBoards) {
                removeAttachments(b);
            }
        }
        List<Topic> topics = board.getTopics();
        if(topics != null) {
            for(Topic t : topics) {
                removeAttachments(t);
            }
        }
    }

    @Override
    public void removeAttachments(Topic topic) {
        if(topic == null) {
            log.debug("Trying remove attachments for null topic");
            return;
        }
        List<Post> posts = topic.getPosts();
        if(posts != null) {
            for(Post post : posts) {
                removeAttachments(post);
            }
        }
    }

    @Override
    public void removeAttachments(Post post) {
        if(post == null) {
            log.debug("Trying remove attachments for null post");
            return;
        }
        List<Attachment> attachments = post.getAttachments();
        List<Integer> ids = new ArrayList<>();
        if(!isEmpty(attachments)) {
            for(Attachment attachment : attachments) {
                fileAttachmentService.delete(attachment);
                ids.add(attachment.getId());
            }
            dbAttachmentService.deleteByIds(ids);
        }
    }
}