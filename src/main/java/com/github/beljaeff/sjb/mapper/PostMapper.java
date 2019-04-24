package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.conversation.PostDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.beljaeff.sjb.util.HttpUtils.makeLink;

@Component
public class PostMapper extends AbstractMapper {

    private AttachmentMapper attachmentMapper;
    private UserMapper userMapper;

    @Autowired
    public void setAttachmentMapper(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void updatePostFromForm(Post post, PostForm form, final String ip, Topic topic, Post parent, User user, List<Attachment> attachments) {
        if(form == null || post == null || !isLoaded(post)) {
            return;
        }

        if(parent != null) {
            post.setParentPost(parent);
        }
        if(topic != null) {
            post.setTopic(topic);
        }
        post.setIpLastUpdate(ip);
        post.setUserLastUpdate(user);
        post.setDateLastUpdate(LocalDateTime.now());
        if(form.getIsActive() != null) {
            post.setIsActive(form.getIsActive());
        }
        if(form.getIsApproved() != null) {
            post.setIsApproved(form.getIsApproved());
        }
        if(form.getIsSticky() != null) {
            post.setIsSticky(form.getIsSticky());
        }
        post.setBody(form.getMessage());
        post.setAttachments(attachments);
    }

    public PostDto postToPostDto(Post post) {
        if(post == null || !isLoaded(post)) {
            return null;
        }
        PostDto result = new PostDto();
        result.setId(post.getId());
        result.setIsActive(post.getIsActive());
        result.setIsApproved(post.getIsApproved());
        result.setIsSticky(post.getIsSticky());
        result.setDateCreate(dateTimeToString(post.getDateCreate()));
        result.setDateLastUpdate(dateTimeToString(post.getDateLastUpdate()));
        if(isLoaded(post.getAuthor())) {
            result.setAuthor(userMapper.userToUserDto(post.getAuthor()));
        }
        result.setBody(post.getBody());
        result.setLink(post.getId() > 0 ? makeLink(EntityType.POST.getType(), post.getId()) : "");
        result.setKarma(post.getGoodKarma() - post.getBadKarma());
        if(isLoaded(post.getAttachments())) {
            result.setAttachments(attachmentMapper.attachmentToAttachmentDto(post.getAttachments()));
        }
        if(result.getAttachments() == null) {
            result.setAttachments(Collections.emptyList());
        }
        return result;
    }

    public PaginatedDto<PostDto> postToPostDto(List<Post> posts) {
        if(!isLoaded(posts)) {
            return null;
        }
        List<PostDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(posts)) {
            for(Post post : posts) {
                PostDto dto = postToPostDto(post);
                if(dto != null) {
                    list.add(dto);
                }
            }
        }
        return new PaginatedDto<>(list.size(), 1, 1, list.size(), list);
    }


    public PaginatedDto<PostDto> postToPostDto(PagedEntityList<Post> posts) {
        if(posts == null) {
            return new PaginatedDto<>();
        }
        PaginatedDto<PostDto> result = postToPostDto(posts.getList());
        result.setCurrentPage(posts.getCurrentPage());
        result.setPageSize(posts.getPageSize());
        result.setTotal(posts.getTotal());
        result.setTotalPages(posts.getTotalPages());

        return result;
    }

    public PostForm postToForm(Post post) {
        if(post == null || !isLoaded(post)) {
            return null;
        }
        PostForm form = new PostForm();
        form.setId(post.getId());
        if(isLoaded(post.getTopic())) {
            form.setIdTopic(post.getTopic().getId());
        }
        if(isLoaded(post.getParentPost()) && post.getParentPost() != null) {
            form.setIdParent(post.getParentPost().getId());
        }
        form.setIsActive(post.getIsActive());
        form.setIsApproved(post.getIsApproved());
        form.setIsSticky(post.getIsSticky());
        form.setMessage(post.getBody());

        return form;
    }
}
