package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.conversation.TopicDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;
import com.github.beljaeff.sjb.dto.form.conversation.TopicForm;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.beljaeff.sjb.util.HttpUtils.makeLink;

@Component
public class TopicMapper extends AbstractMapper {

    private PostMapper postMapper;
    private UserMapper userMapper;

    @Autowired
    public void setPostMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Topic createTopicFromForm(TopicForm form, boolean isApproved, String ip, List<Attachment> attachments, Board board, LocalDateTime now, User user) {
        if(form == null) {
            return null;
        }
        Topic topic = new Topic();
        Post post = new Post();

        post.setIsActive(false);
        post.setIsSticky(false);
        post.setIsApproved(isApproved);
        post.setIpCreate(ip);
        post.setIpLastUpdate(ip);
        post.setBody(form.getMessage());
        post.setAuthor(user);
        post.setUserLastUpdate(user);
        post.setDateCreate(now);
        post.setDateLastUpdate(now);
        post.setAttachments(attachments);
        post.setTopic(topic);

        topic.setIsActive(false);
        topic.setIsSticky(false);
        topic.setIsLocked(false);
        topic.setIsApproved(isApproved);
        topic.setPostsCount(1);
        topic.setTitle(form.getTitle());
        topic.setIcon(form.getIcon());
        topic.setBoard(board);
        topic.setAuthor(user);
        topic.setFirstPost(post);
        topic.setLastPost(post);

        return topic;
    }

    public void updateTopicFromForm(Topic topic, TopicForm form, Board board) {
        if(form == null || topic == null || !isLoaded(topic)) {
            return;
        }
        topic.setTitle(form.getTitle());

        if(board != null) {
            topic.setBoard(board);
        }

        topic.setIcon(form.getIcon());

        if(form.getIsLocked() != null) {
            topic.setIsLocked(form.getIsLocked());
        }
        if(form.getIsSticky() != null) {
            topic.setIsSticky(form.getIsSticky());
        }
        if(form.getIsApproved() != null) {
            topic.setIsApproved(form.getIsApproved());
        }
        if(form.getIsActive() != null) {
            topic.setIsActive(form.getIsActive());
        }
    }

    public TopicForm topicToForm(Topic topic) {
        if(topic == null || !isLoaded(topic)) {
            return null;
        }
        TopicForm form = new TopicForm();
        form.setId(topic.getId());
        if(isLoaded(topic.getBoard())) {
            form.setIdBoard(topic.getBoard().getId());
        }
        form.setIcon(topic.getIcon());
        form.setIsActive(topic.getIsActive());
        form.setIsApproved(topic.getIsApproved());
        form.setIsLocked(topic.getIsLocked());
        form.setIsSticky(topic.getIsSticky());
        form.setTitle(topic.getTitle());

        return form;
    }

    public TopicDto topicToTopicDto(Topic topic) {
        if(topic == null || !isLoaded(topic)) {
            return null;
        }
        TopicDto result = new TopicDto();
        result.setId(topic.getId());
        result.setTitle(topic.getTitle());
        result.setLink(topic.getId() > 0 ? makeLink(EntityType.TOPIC.getType(), topic.getId()) : "");
        result.setIsActive(topic.getIsActive());
        result.setIsApproved(topic.getIsApproved());
        result.setIsLocked(topic.getIsLocked());
        result.setIsSticky(topic.getIsSticky());
        result.setDateCreate(dateTimeToString(topic.getDateCreate()));
        result.setViewsCount((int)topic.getViewsCount());
        result.setPostsCount((int)topic.getPostsCount());
        if(isLoaded(topic.getAuthor())) {
            result.setAuthor(userMapper.userToUserDto(topic.getAuthor()));
        }
        if(isLoaded(topic.getLastPost())) {
            result.setLastPost(postMapper.postToPostDto(topic.getLastPost()));
        }
        if(isLoaded(topic.getPosts())) {
            result.setPosts(postMapper.postToPostDto(topic.getPosts()));
        }
        if(result.getPosts() == null) {
            result.setPosts(new PaginatedDto<>());
        }

        return result;
    }

    PaginatedDto<TopicDto> topicToTopicDto(List<Topic> topics) {
        if(!isLoaded(topics)) {
            return null;
        }
        List<TopicDto> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(topics)) {
            for(Topic topic : topics) {
                TopicDto dto = topicToTopicDto(topic);
                if(dto != null) {
                    list.add(dto);
                }
            }
        }

        return new PaginatedDto<>(list.size(), 1, 1, list.size(), list);
    }

    public PaginatedDto<TopicDto> topicToTopicDto(PagedEntityList<Topic> topics) {
        if(topics == null) {
            return new PaginatedDto<>();
        }
        PaginatedDto<TopicDto> result = topicToTopicDto(topics.getList());
        result.setCurrentPage(topics.getCurrentPage());
        result.setPageSize(topics.getPageSize());
        result.setTotal(topics.getTotal());
        result.setTotalPages(topics.getTotalPages());

        return result;
    }
}