package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.conversation.BoardDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;
import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.mapper.BoardMapper;
import com.github.beljaeff.sjb.mapper.TopicMapper;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.repository.BoardRepository;
import com.github.beljaeff.sjb.repository.PositionableRepository;
import com.github.beljaeff.sjb.repository.TopicRepository;
import com.github.beljaeff.sjb.repository.condition.TopicCondition;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.mapper.CategoryMapper;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractPositionableService;

@Slf4j
@Service("boardService")
public class BoardServiceImpl extends AbstractPositionableService<Board> implements BoardService {
    private final BoardRepository boardRepository;
    private final TopicRepository topicRepository;

    private final CategoryMapper categoryMapper;
    private final BoardMapper boardMapper;
    private final TopicMapper topicMapper;

    private CategoryService categoryService;
    private CommonAttachmentService commonAttachmentService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, TopicRepository topicRepository,
                            CategoryMapper categoryMapper, BoardMapper boardMapper, TopicMapper topicMapper) {
        this.boardRepository = boardRepository;
        this.topicRepository = topicRepository;
        this.categoryMapper= categoryMapper;
        this.boardMapper = boardMapper;
        this.topicMapper = topicMapper;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setCommonAttachmentService(CommonAttachmentService commonAttachmentService) {
        this.commonAttachmentService = commonAttachmentService;
    }

    @Override
    protected PositionableRepository<Board, ? extends Condition> getRepository() {
        return boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<BoardDto> getBoard(int id, int topicPage) {
        Board board = getWithGraph(id, EntityGraphNamesHelper.BOARD_WITH_CHILD_BOARDS);

        TopicCondition condition = getConditionForBoard(id);
        PagedEntityList<Topic> topics = topicRepository.getPageableList(condition, topicPage, EntityGraphNamesHelper.TOPIC_WITH_LAST_POST_AND_AUTHOR);
        if(topicPage < 1 || topicPage > topics.getTotalPages()) {
            log.error("Requested topic page does not exist");
            throw new NotFoundException();
        }
        BoardDto entity = boardMapper.boardToBoardDto(board);
        entity.setTopics(topicMapper.topicToTopicDto(topics));
        return new PageDto<>(entity, entity.getTitle(), getBreadcrumbs(id));
    }

    private TopicCondition getConditionForBoard(int id) {
        TopicCondition condition = new TopicCondition();
        condition.setBoardId(id);
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_TOPIC) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setIsActive(true);
        }
        if(!(UserUtils.hasPermission(BasePermission.APPROVE_TOPIC) || UserUtils.hasPermission(BasePermission.EDIT_TOPIC) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setIsApproved(true);
        }
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_POST) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setPostsIsActive(true);
        }
        if(!(UserUtils.hasPermission(BasePermission.APPROVE_POST) || UserUtils.hasPermission(BasePermission.EDIT_POST) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            condition.setPostsIsApproved(true);
        }
        return condition;
    }

    @Override
    @Transactional(readOnly = true)
    public Board getWithGraph(int id, String entityGraphName) {
        boolean isActive = boardRepository.isEntityActive(id, true);
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_BOARD) || UserUtils.hasPermission(BasePermission.EDIT_BOARD) || UserUtils.hasPermission(
                BasePermission.ADMIN)) && !isActive) {
            log.error("Insufficient permissions to access inactive board {} or its parents for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }
        return super.getWithGraph(id, entityGraphName);
    }

    @Override
    public Board get(int id) {
        return getWithGraph(id, null);
    }

    @Override
    @Transactional(readOnly = true)
    public void removeAttachments(Board board) {
        commonAttachmentService.removeAttachments(board);
    }

    @Override
    @Transactional
    public void create(BoardForm form) {
        save(new Board(), form);
    }

    @Override
    @Transactional
    public void edit(BoardForm form) {
        save(get(form.getId()), form);
    }

    private void save(Board board, BoardForm form) {
        Assert.notNull(form, "board form should be set");

        Board parent = null;
        if(form.getParentBoard() != null) {
            parent = get(form.getParentBoard());
        }
        Category category = null;
        if(form.getCategory() != null) {
            category = categoryService.get(form.getCategory());
        }
        boardMapper.updateBoardFromForm(board, category, parent, form);
        save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, BoardForm> getAddForm(BoardForm formGiven) {
        // Add from category
        String title = recordService.getText("add.board.title");
        if(formGiven.getCategory() != null) {
            Category category = categoryService.get(formGiven.getCategory());
            return new FormPageDto<>(
                    categoryMapper.categoryToCategoryDto(category),
                    formGiven,
                    title,
                    categoryService.getAddBreadcrumbs(formGiven.getCategory(), title));
        }
        // Add from board
        else if(formGiven.getParentBoard() != null) {
            Board parentBoard = get(formGiven.getParentBoard());
            return new FormPageDto<>(
                    boardMapper.boardToBoardDto(parentBoard),
                    formGiven,
                    title,
                    getAddBreadcrumbs(formGiven.getParentBoard(), title));
        }
        // Add from index
        return new FormPageDto<>(new BaseDto() {}, formGiven, title, getAddBreadcrumbs(null, title));
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, BoardForm> getEditForm(int id, BoardForm formGiven) {
        BoardForm form;
        Board board = get(id);

        // Edit form request
        if(id != formGiven.getId()) {
            form = boardMapper.boardToBoardForm(board);
        }
        // Edit form contains errors
        else {
            form = formGiven;
        }
        String title = recordService.getText("edit.board.title");
        return new FormPageDto<>(boardMapper.boardToBoardDto(board), form, title, getEditBreadcrumbs(id, title));
    }
}