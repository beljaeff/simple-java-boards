package com.github.beljaeff.sjb.controller.conversation;

import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.dto.form.conversation.CategoryForm;
import com.github.beljaeff.sjb.service.conversation.CategoryService;

import javax.validation.Valid;

import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_ADD;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_CHANGE_ACTIVE;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_DELETE;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_DOWN;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_EDIT;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_SAVE;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_UP;
import static com.github.beljaeff.sjb.controller.Routes.CATEGORY_VIEW;
import static com.github.beljaeff.sjb.controller.Routes.ROOT_URL;

@Controller
public class CategoryController extends AbstractController {

    private static final String INDEX_TPL         = "index";
    private static final String CATEGORY_VIEW_TPL = "category/view";
    private static final String CATEGORY_EDIT_TPL = "category/edit";

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService service) {
        this.categoryService = service;
    }

    //^/([a-zA-Z_0-9\-]+/?)*$ <-- global regexp for all
    @PreAuthorize("hasAnyPermissions('VIEW', 'ADMIN')")
    @GetMapping(ROOT_URL)
    public String getList(Model model) {
        model.addAttribute(ATTR_PAGE, categoryService.getTopLevelList());
        return INDEX_TPL;
    }

    @PreAuthorize("hasAnyPermissions('VIEW', 'ADMIN')")
    @GetMapping(CATEGORY_VIEW)
    public String get(@PathVariable int id, Model model) {
        model.addAttribute(ATTR_PAGE, categoryService.getCategory(id));
        return CATEGORY_VIEW_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_ADD)
    public String add(Model model) {
        CategoryForm form = getFormFromModel(model, CategoryForm::new);
        FormPageDto<? extends BaseDto, CategoryForm> page = categoryService.getAddForm(form);
        setFormAttributes(page, model);
        return CATEGORY_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_EDIT)
    public String edit(@PathVariable int id, Model model) {
        CategoryForm form = getFormFromModel(model, CategoryForm::new);
        FormPageDto<? extends BaseDto, CategoryForm> page = categoryService.getEditForm(id, form);
        setFormAttributes(page, model);
        return CATEGORY_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @PostMapping(CATEGORY_SAVE)
    public String save(@ModelAttribute(ATTR_FORM) @Valid CategoryForm form, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, form.getId() == 0 ? categoryService::create : categoryService::edit);
    }

    protected void setFormUrls(BaseForm baseForm) {
        CategoryForm form = (CategoryForm)baseForm;
        String formUrl = form.getId() == 0
                ? CATEGORY_ADD
                : buildSegmentsPath(CATEGORY_EDIT, form.getId());
        form.setFormUrl(formUrl);
        form.setSaveUrl(CATEGORY_SAVE);
        form.setCancelUrl(ROOT_URL);
        form.setFinishUrl(ROOT_URL);
    }

    @Override
    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_SAVE)
    public String redirect(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('DELETE_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_DELETE)
    public String delete(@PathVariable int id) {
        categoryService.delete(id);
        return "redirect:" + ROOT_URL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ACTIVATE_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_CHANGE_ACTIVE)
    public String changeActive(@PathVariable int id) {
        categoryService.changeActive(id);
        return "redirect:" + ROOT_URL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_UP)
    public String up(@PathVariable int id) {
        categoryService.up(id);
        return "redirect:" + ROOT_URL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_CATEGORY', 'ADMIN')")
    @GetMapping(CATEGORY_DOWN)
    public String down(@PathVariable int id) {
        categoryService.down(id);
        return "redirect:" + ROOT_URL;
    }
}