package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.conversation.CategoryDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.conversation.CategoryForm;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.service.PositionableService;

public interface CategoryService extends PositionableService<Category>, CanCreateSaveForm<CategoryForm> {
    PageDto<BaseDto> getTopLevelList();
    PageDto<CategoryDto> getCategory(int id);
    FormPageDto<? extends BaseDto, CategoryForm> getAddForm(CategoryForm form);
    FormPageDto<? extends BaseDto, CategoryForm> getEditForm(int id, CategoryForm form);
}