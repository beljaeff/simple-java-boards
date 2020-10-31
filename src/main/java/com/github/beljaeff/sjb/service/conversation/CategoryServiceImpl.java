package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.ListDto;
import com.github.beljaeff.sjb.dto.dto.conversation.CategoryDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.conversation.CategoryForm;
import com.github.beljaeff.sjb.enums.BasePermission;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.repository.BoardRepository;
import com.github.beljaeff.sjb.repository.CategoryRepository;
import com.github.beljaeff.sjb.repository.PositionableRepository;
import com.github.beljaeff.sjb.repository.condition.BoardCondition;
import com.github.beljaeff.sjb.repository.condition.CategoryCondition;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import com.github.beljaeff.sjb.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.beljaeff.sjb.mapper.CategoryMapper;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractPositionableService;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class CategoryServiceImpl extends AbstractPositionableService<Category> implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    private CommonAttachmentService commonAttachmentService;

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BoardRepository boardRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.boardRepository = boardRepository;
        this.categoryMapper = categoryMapper;
    }

    @Autowired
    public void setCommonAttachmentService(CommonAttachmentService commonAttachmentService) {
        this.commonAttachmentService = commonAttachmentService;
    }

    private Category getFakeCategory() {
        Category category = new Category();
        category.setPosition(Integer.MAX_VALUE);
        category.setTitle(recordService.getText("uncategorized.category"));
        category.setIsActive(true);
        return category;
    }

    @Override
    protected PositionableRepository<Category, ? extends Condition> getRepository() {
        return categoryRepository;
    }

    /**
     * Get top level categories with boards inside include boards with no categories
     * @return resulting list
     */
    @Override
    @Transactional(readOnly = true)
    public PageDto<BaseDto> getTopLevelList() {
        // Get top level categories with boards
        CategoryCondition categoryCondition = new CategoryCondition();
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_CATEGORY) || UserUtils.hasPermission(BasePermission.EDIT_CATEGORY) || UserUtils
                .hasPermission(BasePermission.ADMIN))) {
            categoryCondition.setIsActive(true);
        }
        final List<Category> categoryList = categoryRepository.getList(categoryCondition, EntityGraphNamesHelper.CATEGORIES_WITH_BOARDS);

        // Get top level boards without category and parent
        BoardCondition boardCondition = new BoardCondition();
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_BOARD) || UserUtils.hasPermission(BasePermission.EDIT_BOARD) || UserUtils.hasPermission(
                BasePermission.ADMIN))) {
            boardCondition.setIsActive(true);
        }
        List<Board> boards = boardRepository.getList(boardCondition, EntityGraphNamesHelper.BOARDS_WITH_LAST_TOPIC);
        if(!isEmpty(boards)) {
            Category fakeCategory = getFakeCategory();
            fakeCategory.setBoards(boards);
            categoryList.add(fakeCategory);
        }

        BaseDto entity = new ListDto(categoryMapper.categoryToCategoryDto(categoryList));
        return new PageDto<>(entity, recordService.getText("categories.title"), getBreadcrumbs(null));
    }

    /**
     * Get category with boards
     * @return resulting category
     */
    @Override
    public PageDto<CategoryDto> getCategory(int id) {
        CategoryDto category = categoryMapper.categoryToCategoryDto(getWithGraph(id, EntityGraphNamesHelper.CATEGORIES_WITH_BOARDS));
        return new PageDto<>(category, category.getTitle(), getBreadcrumbs(id));
    }

    /**
     * Get category using graph.
     * @param id category id
     * @param graphName entity graph using to select category
     * @return resulting category
     */
    @Override
    public Category getWithGraph(int id, String graphName) {
        Category category = super.getWithGraph(id, graphName);
        if(!(UserUtils.hasPermission(BasePermission.ACTIVATE_CATEGORY) || UserUtils.hasPermission(BasePermission.EDIT_CATEGORY) || UserUtils
                .hasPermission(BasePermission.ADMIN)) && !category.getIsActive()) {
            log.error("Insufficient permissions to access inactive category {} for {}", id, UserUtils.getCurrentUser().getUser().getNickName());
            throw new NotFoundException();
        }
        return category;
    }

    @Transactional
    @Override
    public void create(CategoryForm form) {
        save(new Category(), form);
    }

    @Transactional
    @Override
    public void edit(CategoryForm form) {
        save(get(form.getId()), form);
    }

    private void save(Category category, CategoryForm form) {
        categoryMapper.updateCategoryFromForm(category, form);
        save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public void removeAttachments(Category category) {
        commonAttachmentService.removeAttachments(category);
    }

    @Override
    public FormPageDto<? extends BaseDto, CategoryForm> getAddForm(CategoryForm formGiven) {
        String title = recordService.getText("add.category.title");
        return new FormPageDto<>(new BaseDto() {}, formGiven, title, getAddBreadcrumbs(null, title));
    }

    @Override
    @Transactional(readOnly = true)
    public FormPageDto<? extends BaseDto, CategoryForm> getEditForm(int id, CategoryForm formGiven) {
        CategoryForm form;
        Category c = get(id);

        // Edit form request
        if(id != formGiven.getId()) {
            form = categoryMapper.categoryToCategoryForm(c);
        }
        // Edit form contains errors
        else {
            form = formGiven;
        }
        String title = recordService.getText("edit.category.title");
        return new FormPageDto<>(categoryMapper.categoryToCategoryDto(c), form, title, getEditBreadcrumbs(id, title));
    }
}