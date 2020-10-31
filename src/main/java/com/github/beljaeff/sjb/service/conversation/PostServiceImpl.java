package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.conversation.TopicDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.ForbiddenException;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.mapper.PostMapper;
import com.github.beljaeff.sjb.mapper.TopicMapper;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.BaseRepository;
import com.github.beljaeff.sjb.repository.PostRepository;
import com.github.beljaeff.sjb.repository.UserRepository;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractHasAttachmentsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl extends AbstractHasAttachmentsService<Post> implements PostService {

    @Value("${allow.anon.post.without.approve}")
    private boolean allowAnonPostWithoutApprove = false;

    @Value("${allow.post.without.approve}")
    private boolean allowPostWithoutApprove = true;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final TopicMapper topicMapper;
    private final PostMapper postMapper;
    private final HttpServletRequest request;

    private TopicService topicService;
    private CommonAttachmentService commonAttachmentService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, TopicMapper topicMapper, PostMapper postMapper, HttpServletRequest request) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicMapper = topicMapper;
        this.postMapper = postMapper;
        this.request = request;
    }

    @Autowired
    public void setTopicService(TopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    public void setCommonAttachmentService(CommonAttachmentService commonAttachmentService) {
        this.commonAttachmentService = commonAttachmentService;
    }

    @Override
    protected BaseRepository<Post, ? extends Condition> getRepository() {
        return postRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Post get(int id) {
        Post post = postRepository.get(id, EntityGraphNamesHelper.POST_WITH_ATTACHMENTS_AND_TOPIC_WITH_BOARD);
        boolean isAncestorsActive = post != null && postRepository.isEntityActive(id, true);
        //TODO check access to restricted board (private access)
        if(post == null ||
           !(UserUtils.hasPermission(BasePermission.ACTIVATE_POST) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                   BasePermission.ADMIN)) && !post.getIsActive() ||
           !(UserUtils.hasPermission(BasePermission.APPROVE_POST) || UserUtils.hasPermission(BasePermission.ADMIN)) && !post.getIsApproved() ||
           !(UserUtils.hasPermission(BasePermission.ACTIVATE_TOPIC) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                   BasePermission.ADMIN)) && !post.getTopic().getIsActive() ||
           !(UserUtils.hasPermission(BasePermission.APPROVE_TOPIC) || UserUtils.hasPermission(BasePermission.ADMIN)) && !post.getTopic().getIsApproved() ||
           !(UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(BasePermission.ADMIN)) && !isAncestorsActive) {
            log.error("Insufficient permissions to access inactive post {} or its parents for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }
        return post;
    }

    @Override
    @Transactional
    public void create(PostForm form) {
        Assert.notNull(form, "post form should be set");

        String ip = request.getRemoteAddr();
        Post post = new Post();

        boolean isApproved = UserUtils.isAnonymous() && allowAnonPostWithoutApprove ||
                             !UserUtils.isAnonymous() && allowPostWithoutApprove ||
                             UserUtils.hasPermission(BasePermission.ADMIN);
        post.setIsApproved(isApproved);

        post.setIsActive(false);
        post.setIsSticky(false);
        post.setIpCreate(ip);
        post.setIpLastUpdate(ip);

        post.setBody(form.getMessage());

        User currentUser = UserUtils.getCurrentUser().getUser();
        User user = userRepository.get(currentUser.getId());
        post.setAuthor(user);
        post.setUserLastUpdate(user);

        LocalDateTime now = LocalDateTime.now();
        post.setDateCreate(now);
        post.setDateLastUpdate(now);

        Topic topic = topicService.get(form.getIdTopic());
        if(topic.getIsLocked()) {
            log.error("Adding post to locked topic '{}' request attempt by '{}'", topic.getId(), UserUtils.getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }
        post.setTopic(topic);

        Post parent = form.getIdParent() != null ? get(form.getIdParent()) : null;
        post.setParentPost(parent);

        //TODO: attachemnts
        List<Attachment> attachments = commonAttachmentService.createPostAttachments(form.getAttachments());
        post.setAttachments(attachments);

        postRepository.add(post);
        if(topic.getFirstPost() == null) {
            topic.setFirstPost(post);
        }
        topic.setLastPost(post);
        topic.setPostsCount(topic.getPostsCount() + 1);
        topicService.save(topic);
        user.setPostsCount(user.getPostsCount() + 1);
        userRepository.update(user);
        currentUser.setPosts(user.getPosts());

        form.setToPage(topicService.getLastPage(form.getIdTopic()));
   }

    @Override
    @Transactional
    public void edit(PostForm form) {
        Assert.notNull(form, "edit post form should be set");

        String ip = request.getRemoteAddr();
        Post post = get(form.getId());
        User currentUser = UserUtils.getCurrentUser().getUser();
        User user = userRepository.get(currentUser.getId());

        if(UserUtils.hasPermission(BasePermission.EDIT_OWN_POST) &&
          !UserUtils.hasPermission(BasePermission.ADMIN) &&
          !UserUtils.hasPermission(BasePermission.EDIT_POST)) {
            if(post.getAuthor().getId() != UserUtils.getCurrentUser().getId()) {
                log.error("Not owner '{}' post '{}' modifying request attempt ", UserUtils.getCurrentUser().getUser().getNickName(), form.getId());
                throw new ForbiddenException();
            }
            if(post.getParentPost() != null) {
                form.setIdParent(post.getParentPost().getId());
            }
            form.setIsApproved(post.getIsApproved());
            form.setIsActive(post.getIsActive());
            if(!UserUtils.hasPermission(BasePermission.MAKE_STICKY_OWN_POST)) {
                form.setIsSticky(post.getIsSticky());
            }
        }
        if(!UserUtils.hasPermission(BasePermission.MOVE_POST) && !UserUtils.hasPermission(BasePermission.MOVE_POST) && form.getIdTopic() != post.getTopic().getId()) {
            form.setIdTopic(post.getTopic().getId());
        }

        Topic topic = getTopic(form.getIdTopic());
        Post parent = getParent(form.getIdParent());
        List<Attachment> attachments = commonAttachmentService.mergePostAttachments(form.getAttachments(), post.getAttachments());
        postMapper.updatePostFromForm(post, form, ip, topic, parent, user, attachments);
        postRepository.update(post);
    }

    private Topic getTopic(Integer idTopic) {
        Topic topic = null;
        if(idTopic != null) {
            topic = topicService.get(idTopic);
            if(topic.getIsLocked()) {
                log.error("Edting post from locked topic '{}' request attempt by '{}'", topic.getId(), UserUtils.getCurrentUser().getUser().getNickName());
                throw new ForbiddenException();
            }
        }
        return topic;
    }

    private Post getParent(Integer idParent) {
        return idParent != null ? get(idParent) : null;
    }

    @Override
    @Transactional
    public ActionStatusDto<Post> approve(int id) {
        Post post = get(id);
        ActionStatusDto<Post> actionStatus = new ActionStatusDto<>(post, false);
        if(!post.getIsApproved()) {
            post.setIsApproved(true);
            save(post);
            actionStatus.setStatus(true);
        }
        return actionStatus;
    }

    @Override
    @Transactional
    public ActionStatusDto<Post> changeSticky(int id) {
        Post post = get(id);
        if(!(UserUtils.hasPermission(BasePermission.EDIT_OWN_POST) && UserUtils.hasPermission(BasePermission.MAKE_STICKY_OWN_POST) && post.getAuthor().getId() == UserUtils
                .getCurrentUser().getId() ||
             UserUtils.hasPermission(BasePermission.ADMIN) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                BasePermission.MAKE_STICKY_POST))) {
            log.error("Insufficient permissions to change sticky for post {} for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }
        ActionStatusDto<Post> actionStatus = new ActionStatusDto<>(post, false);
        post.setIsSticky(!post.getIsSticky());
        save(post);
        actionStatus.setStatus(true);

        return actionStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public void removeAttachments(Post post) {
        commonAttachmentService.removeAttachments(post);
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, PostForm> getCreateForm(int idReplyPost, PostForm formGiven) {
        int replyPostId = formGiven.getIdParent() == null ? idReplyPost : formGiven.getIdParent();
        Post parentPost = get(replyPostId);

        formGiven.setIdParent(parentPost.getId());
        formGiven.setIdTopic(parentPost.getTopic().getId());

        TopicDto pageDto = topicMapper.topicToTopicDto(parentPost.getTopic());
        String title = recordService.getText("reply.post.title");
        return new FormPageDto<>(pageDto, formGiven, title, topicService.getAddBreadcrumbs(parentPost.getTopic().getId(), title));
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, PostForm> getEditForm(int idPost, PostForm formGiven) {
        Post post = get(idPost);
        PostForm form;
        // Edit form request
        if(idPost != formGiven.getId()) {
            form = postMapper.postToForm(post);
        }
        // Edit form contains errors
        else {
            form = formGiven;
        }
        String title = recordService.getText("edit.post.title");
        return new FormPageDto<>(postMapper.postToPostDto(post), form, title, getEditBreadcrumbs(idPost, title));
    }

    @Override
    @Transactional
    public ActionStatusDto<Post> removeAttachment(int id, int attachmentId) {
        Post post = getWithGraph(id, EntityGraphNamesHelper.POST_WITH_AUTHOR_AND_ATTACHMENTS);

        if(post == null || CollectionUtils.isEmpty(post.getAttachments())) {
            log.error("Remove attachment {} for null post request attempt by {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }

        if(!UserUtils.hasPermission(BasePermission.ADMIN) && !UserUtils.hasPermission(BasePermission.EDIT_POST) &&
           (post.getAuthor() == null || post.getAuthor().getId() != UserUtils.getCurrentUser().getUser().getId())) {
            log.error("Remove attachment {} for not owner request attempt by {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }

        boolean isFound = false;
        for(Attachment attachment : post.getAttachments()) {
            if(attachment.getId() == attachmentId) {
                isFound = true;
                break;
            }
        }
        if(!isFound) {
            log.error("Remove not existing attachment request attempt by {}", UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }

        ActionStatusDto<Attachment> actionStatusDto = commonAttachmentService.delete(attachmentId);

        return new ActionStatusDto<>(post, actionStatusDto.getStatus());
    }
}