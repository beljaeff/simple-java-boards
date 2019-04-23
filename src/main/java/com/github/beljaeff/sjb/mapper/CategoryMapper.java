package com.github.beljaeff.sjb.mapper;

import com.github.beljaeff.sjb.dto.dto.conversation.CategoryDto;
import com.github.beljaeff.sjb.dto.form.conversation.CategoryForm;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper extends AbstractMapper {

    private BoardMapper boardMapper;

    @Autowired
    public void setBoardMapper(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public void updateCategoryFromForm(Category category, CategoryForm form) {
        if(form == null || category == null || !isLoaded(category)) {
            return;
        }
        category.setTitle(form.getTitle());
        category.setIsActive(form.getIsActive());
        category.setPosition(form.getPosition() == null ? 0 : Integer.parseInt(form.getPosition()));
    }

    public CategoryForm categoryToCategoryForm(Category category) {
        if(!isLoaded(category) || category == null) {
            return null;
        }
        CategoryForm form = new CategoryForm();
        form.setId(category.getId());
        form.setTitle(category.getTitle());
        form.setIsActive(category.getIsActive());
        form.setPosition(String.valueOf(category.getPosition()));

        return form;
    }

    public List<CategoryDto> categoryToCategoryDto(List<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();
        if(!isLoaded(categories)) {
            return result;
        }
        if(!CollectionUtils.isEmpty(categories)) {
            for(Category category : categories) {
                CategoryDto dto = categoryToCategoryDto(category);
                if(dto != null) {
                    result.add(dto);
                }
            }
        }
        return result;
    }

    public CategoryDto categoryToCategoryDto(Category category) {
        if(!isLoaded(category) || category == null) {
            return null;
        }
        CategoryDto result = new CategoryDto();
        result.setId(category.getId());
        result.setTitle(category.getTitle());
        result.setLink(category.getId() > 0 ? HttpUtils.makeLink(EntityType.CATEGORY.getType(), category.getId()) : "");
        result.setIsActive(category.getIsActive());
        if(isLoaded(category.getBoards())) {
            result.setBoards(boardMapper.boardToBoardDto(category.getBoards()));
        }
        return result;
    }
}
