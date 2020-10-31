package com.github.beljaeff.sjb.controller.conversation;

import com.github.beljaeff.sjb.controller.RoutesHelper;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.github.beljaeff.sjb.dto.dto.conversation.TopicDto;
import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.dto.form.conversation.TopicForm;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.service.conversation.TopicService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class TopicController extends AbstractController {

    private static final String TOPIC_VIEW_TPL   = "topic/view";
    private static final String TOPIC_CREATE_TPL = "topic/create";
    private static final String TOPIC_EDIT_TPL   = "topic/edit";

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PreAuthorize("hasAnyPermissions('VIEW', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_VIEW)
    public String get(@PathVariable int id, @RequestParam(name = ATTR_PAGE, defaultValue = "1") int topicPage, Model model) {
        PostForm form = getFormFromModel(model, PostForm::new);
        FormPageDto<TopicDto, PostForm> page = topicService.getTopic(id, topicPage, form);
        page.getForm().setFromPage(topicPage);
        page.getForm().setIdTopic(id);
        page.getForm().setSaveUrl(RoutesHelper.POST_SAVE_REPLY);

        model.addAttribute(ATTR_PAGE, page);
        model.addAttribute(ATTR_FORM, page.getForm());

        return TOPIC_VIEW_TPL;
    }

    @PreAuthorize("hasAnyPermissions('CREATE_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_CREATE)
    public String add(@PathVariable int id, Model model) {
        TopicForm form = getFormFromModel(model, TopicForm::new);
        FormPageDto<? extends BaseDto, TopicForm> page = topicService.getCreateForm(id, form);
        setFormAttributes(page, model);
        return TOPIC_CREATE_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'EDIT_OWN_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_EDIT)
    public String edit(@PathVariable int id, Model model) {
        TopicForm form = getFormFromModel(model, TopicForm::new);
        FormPageDto<? extends BaseDto, TopicForm> page = topicService.getEditForm(id, form);
        setFormAttributes(page, model);
        return TOPIC_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('CREATE_TOPIC', 'ADMIN')")
    @PostMapping(RoutesHelper.TOPIC_SAVE_NEW)
    public String saveAdd(@ModelAttribute(ATTR_FORM) @Valid TopicForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, topicService::create);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'EDIT_OWN_TOPIC', 'ADMIN')")
    @PostMapping(RoutesHelper.TOPIC_SAVE)
    public String saveEdit(@ModelAttribute(ATTR_FORM) @Valid TopicForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, topicService::edit);
    }

    protected void setFormUrls(BaseForm baseForm) {
        TopicForm form = (TopicForm)baseForm;
        if(form.getId() == 0) {
            form.setFormUrl(buildSegmentsPath(RoutesHelper.TOPIC_CREATE, form.getIdBoard()));
            form.setSaveUrl(RoutesHelper.TOPIC_SAVE_NEW);
            form.setCancelUrl(buildSegmentsPath(RoutesHelper.BOARD_VIEW, form.getIdBoard()));
            form.setFinishUrl(buildSegmentsPath(RoutesHelper.BOARD_VIEW, form.getIdBoard()));
        }
        else {
            form.setFormUrl(buildSegmentsPath(RoutesHelper.TOPIC_EDIT, form.getId()));
            form.setSaveUrl(RoutesHelper.TOPIC_SAVE);
            form.setCancelUrl(buildSegmentsPath(RoutesHelper.TOPIC_VIEW, form.getId()));
            form.setFinishUrl(buildSegmentsPath(RoutesHelper.TOPIC_VIEW, form.getId()));
        }
    }

    @PreAuthorize("hasAnyPermissions('CREATE_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_SAVE_NEW)
    public String redirectAdd(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'EDIT_OWN_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_SAVE)
    public String redirectEdit(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('DELETE_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_DELETE)
    public String delete(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(page, topicService.delete(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'ACTIVATE_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_CHANGE_ACTIVE)
    public String changeActive(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(page, topicService.changeActive(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'LOCK_TOPIC', 'ADMIN') || hasPermission('LOCK_OWN_TOPIC') && hasPermission('EDIT_OWN_TOPIC')")
    @GetMapping(RoutesHelper.TOPIC_CHANGE_LOCK)
    public String changeLock(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(page, topicService.changeLock(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_TOPIC', 'MAKE_STICKY_TOPIC', 'ADMIN') || hasPermission('MAKE_STICKY_OWN_TOPIC') && hasPermission('EDIT_OWN_TOPIC')")
    @GetMapping(RoutesHelper.TOPIC_CHANGE_STICKY)
    public String changeSticky(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(page, topicService.changeSticky(id));
    }

    @PreAuthorize("hasAnyPermissions('APPROVE_TOPIC', 'ADMIN')")
    @GetMapping(RoutesHelper.TOPIC_APPROVE)
    public String approve(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(page, topicService.approve(id));
    }

    private String makeLinkForRedirect(Integer page, ActionStatusDto<Topic> actionStatus) {
        if(actionStatus == null || actionStatus.getEntity() == null || actionStatus.getEntity().getBoard() == null) {
            return RoutesHelper.ROOT_URL;
        }
        if(page != null) {
            Map<String, Object> uriParams = Map.of(ATTR_ID, actionStatus.getEntity().getBoard().getId(), ATTR_PAGE, page);
            return buildSegmentsQueryPath(RoutesHelper.BOARD_VIEW, RoutesHelper.PAGE_QUERY, uriParams);
        }
        else {
            return buildSegmentsPath(RoutesHelper.BOARD_VIEW, actionStatus.getEntity().getBoard().getId());
        }
    }
}