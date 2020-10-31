package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.conversation.BoardDto;
import com.github.beljaeff.sjb.dto.dto.conversation.TopicDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.dto.form.conversation.TopicForm;
import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.ForbiddenException;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.mapper.BoardMapper;
import com.github.beljaeff.sjb.mapper.PostMapper;
import com.github.beljaeff.sjb.mapper.TopicMapper;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.BaseRepository;
import com.github.beljaeff.sjb.repository.PostRepository;
import com.github.beljaeff.sjb.repository.TopicRepository;
import com.github.beljaeff.sjb.repository.condition.PostCondition;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import com.github.beljaeff.sjb.service.security.UserService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractHasAttachmentsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TopicServiceImpl extends AbstractHasAttachmentsService<Topic> implements TopicService {

    @Value("${allow.anon.create.topic.without.approve}")
    private boolean allowAnonCreateTopicWithoutApprove = false;

    @Value("${allow.create.topic.without.approve}")
    private boolean allowCreateTopicWithoutApprove = true;

    private final TopicRepository topicRepository;
    private final PostRepository postRepository;

    private final BoardMapper boardMapper;
    private final TopicMapper topicMapper;
    private final PostMapper postMapper;
    private final HttpServletRequest request;

    private BoardService boardService;
    private UserService userService;
    private CommonAttachmentService commonAttachmentService;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, PostRepository postRepository,
                            BoardMapper boardMapper, TopicMapper topicMapper, PostMapper postMapper,
                            HttpServletRequest request) {
        this.topicRepository = topicRepository;
        this.postRepository  = postRepository;
        this.boardMapper = boardMapper;
        this.topicMapper = topicMapper;
        this.postMapper = postMapper;
        this.request = request;
    }

    // To resolve circular dependencies during injection
    @Autowired
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCommonAttachmentService(CommonAttachmentService commonAttachmentService) {
        this.commonAttachmentService = commonAttachmentService;
    }

    @Override
    protected BaseRepository<Topic, ? extends Condition> getRepository() {
        return topicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<TopicDto, PostForm> getTopic(int id, int postPage, PostForm form) {
        Topic topic = get(id);

        PostCondition condition = new PostCondition();
        condition.setTopicId(id);
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_POST) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setIsActive(true);
        }
        if(!(UserUtils.hasPermission(BasePermission.APPROVE_POST) || UserUtils.hasPermission(BasePermission.ADMIN))) {
            condition.setIsApproved(true);
        }

        PagedEntityList<Post> posts = postRepository.getPageableList(condition, postPage, EntityGraphNamesHelper.POST_EXCEPT_TOPIC_AND_CHILD_POSTS);
        if(postPage < 1 || postPage > posts.getTotalPages()) {
            log.error("Requested post page does not exist");
            throw new NotFoundException();
        }

        TopicDto entity = topicMapper.topicToTopicDto(topic);
        entity.setPosts(postMapper.postToPostDto(posts));
        return new FormPageDto<>(entity, form, entity.getTitle(), getBreadcrumbs(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic get(int id) {
        Topic topic = topicRepository.get(id, EntityGraphNamesHelper.TOPIC_WITH_BOARD_AND_AUTHOR);
        boolean isActive = topic != null && topicRepository.isEntityActive(id, true);
        //TODO check access to restricted board (private access)
        if(topic == null ||
           !(UserUtils.hasPermission(BasePermission.ACTIVATE_TOPIC) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                   BasePermission.ADMIN)) && !topic.getIsActive() ||
           !(UserUtils.hasPermission(BasePermission.APPROVE_TOPIC) || UserUtils.hasPermission(BasePermission.ADMIN)) && !topic.getIsApproved() ||
           !(UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(BasePermission.ADMIN)) && !isActive && (!UserUtils
                   .hasPermission(BasePermission.ACTIVATE_TOPIC) && !topic.getIsActive())) {
            log.error("Insufficient permissions to access inactive topic {} or its parents for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }
        return topic;
    }

    @Override
    @Transactional
    public void create(TopicForm form) {
        Assert.notNull(form, "create topic form should be set");

        boolean isApproved = UserUtils.isAnonymous() && allowAnonCreateTopicWithoutApprove ||
                             !UserUtils.isAnonymous() && allowCreateTopicWithoutApprove ||
                             UserUtils.hasPermission(BasePermission.ADMIN);

        List<Attachment> attachments = commonAttachmentService.createPostAttachments(form.getAttachments());
        Topic topic = topicMapper.createTopicFromForm(form, isApproved, request.getRemoteAddr(), attachments, boardService.get(form.getIdBoard()),
                                                      LocalDateTime.now(), UserUtils.getCurrentUser().getUser());
        save(topic);

        User user = userService.get(UserUtils.getCurrentUser().getUser().getId());
        user.setPostsCount(user.getPostsCount()+1);
        userService.save(user);
        UserUtils.getCurrentUser().getUser().setPosts(user.getPosts());
    }

    @Override
    @Transactional
    public void edit(TopicForm form) {
        Assert.notNull(form, "edit topic form should be set");
        Topic topic = get(form.getId());
        if(!UserUtils.hasPermission(BasePermission.MOVE_TOPIC)) {
            form.setIdBoard(null);
        }
        // Check topic board
        Board board = null;
        if(form.getIdBoard() != null) {
            board = boardService.get(form.getIdBoard());
        }
        if(UserUtils.hasPermission(BasePermission.EDIT_OWN_TOPIC) && !UserUtils.hasPermission(BasePermission.ADMIN) && !UserUtils.hasPermission(
                BasePermission.EDIT_TOPIC)) {
            if(topic.getAuthor().getId() != UserUtils.getCurrentUser().getId()) {
                log.error("Not owner '{}' topic '{}' modifying request attempt ", UserUtils.getCurrentUser().getUser().getNickName(), form.getId());
                throw new ForbiddenException();
            }
            form.setIsApproved(null);
            form.setIsActive(null);
            if(!UserUtils.hasPermission(BasePermission.LOCK_OWN_TOPIC)) {
                form.setIsLocked(null);
            }
            if(!UserUtils.hasPermission(BasePermission.MAKE_STICKY_OWN_TOPIC)) {
                form.setIsSticky(null);
            }
        }
        topicMapper.updateTopicFromForm(topic, form, board);
        save(topic);
    }

    @Override
    @Transactional
    public ActionStatusDto<Topic> approve(int id) {
        Topic topic = get(id);
        ActionStatusDto<Topic> actionStatus = new ActionStatusDto<>(topic, false);
        if(!topic.getIsApproved()) {
            topic.setIsApproved(true);
            save(topic);
            actionStatus.setStatus(true);
        }
        return actionStatus;
    }

    @Override
    @Transactional
    public ActionStatusDto<Topic> changeLock(int id) {
        Topic topic = get(id);
        if(!(UserUtils.hasPermission(BasePermission.EDIT_OWN_TOPIC) && UserUtils.hasPermission(BasePermission.LOCK_OWN_TOPIC) && topic.getAuthor().getId() == UserUtils
                .getCurrentUser().getId() ||
             UserUtils.hasPermission(BasePermission.ADMIN) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                BasePermission.LOCK_TOPIC))) {
            log.error("Insufficient permissions to change lock for topic {} for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }
        ActionStatusDto<Topic> actionStatus = new ActionStatusDto<>(topic, false);
        topic.setIsLocked(!topic.getIsLocked());
        save(topic);
        actionStatus.setStatus(true);

        return actionStatus;
    }

    @Override
    @Transactional
    public ActionStatusDto<Topic> changeSticky(int id) {
        Topic topic = get(id);
        if(!(UserUtils.hasPermission(BasePermission.EDIT_OWN_TOPIC) && UserUtils.hasPermission(BasePermission.MAKE_STICKY_OWN_TOPIC) && topic.getAuthor().getId() == UserUtils
                .getCurrentUser().getId() ||
             UserUtils.hasPermission(BasePermission.ADMIN) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                BasePermission.MAKE_STICKY_TOPIC))) {
            log.error("Insufficient permissions to change sticky for topic {} for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new ForbiddenException();
        }
        ActionStatusDto<Topic> actionStatus = new ActionStatusDto<>(topic, false);
        topic.setIsSticky(!topic.getIsSticky());
        save(topic);
        actionStatus.setStatus(true);

        return actionStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public void removeAttachments(Topic topic) {
        commonAttachmentService.removeAttachments(topic);
    }

    @Override
    public long getTopicsCountForUser(int userId) {
        return topicRepository.getCountForUser(userId);
    }

    @Override
    public int getLastPage(int idTopic) {
        PostCondition condition = new PostCondition();
        condition.setTopicId(idTopic);
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_POST) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setIsActive(true);
        }
        if(!(UserUtils.hasPermission(BasePermission.APPROVE_POST) || UserUtils.hasPermission(BasePermission.ADMIN))) {
            condition.setIsApproved(true);
        }

        return topicRepository.getLastPage(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, TopicForm> getCreateForm(int idBoard, TopicForm formGiven) {
        int boardId = formGiven.getIdBoard() == null ? idBoard : formGiven.getIdBoard();
        formGiven.setIdBoard(boardId);

        BoardDto pageDto = boardMapper.boardToBoardDto(boardService.get(boardId));
        String title = recordService.getText("create.topic.title");
        return new FormPageDto<>(pageDto, formGiven, title, boardService.getAddBreadcrumbs(idBoard, title));
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, TopicForm> getEditForm(int idTopic, TopicForm formGiven) {
        Topic topic = get(idTopic);
        TopicForm form;
        // Edit form request
        if(idTopic != formGiven.getId()) {
            form = topicMapper.topicToForm(topic);
        }
        // Edit form contains errors
        else {
            form = formGiven;
        }
        String title = recordService.getText("edit.topic.title");
        return new FormPageDto<>(topicMapper.topicToTopicDto(topic), form, title, getEditBreadcrumbs(idTopic, title));
    }
}