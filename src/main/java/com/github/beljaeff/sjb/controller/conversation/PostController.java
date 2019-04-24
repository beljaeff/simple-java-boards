package com.github.beljaeff.sjb.controller.conversation;

import com.github.beljaeff.sjb.controller.Routes;
import com.github.beljaeff.sjb.controller.common.BaseController;
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
import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.service.conversation.PostService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class PostController extends AbstractController {

    private static final String POST_REPLY_TPL = "post/reply";
    private static final String POST_EDIT_TPL  = "post/edit";

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasAnyPermissions('CREATE_POST', 'ADMIN')")
    @GetMapping(Routes.POST_REPLY)
    public String add(@PathVariable int id, @RequestParam(name = BaseController.ATTR_PAGE, required = false, defaultValue = "1") int fromPage, Model model) {
        PostForm form = getFormFromModel(model, PostForm::new);
        FormPageDto<? extends BaseDto, PostForm> page = postService.getCreateForm(id, form);
        page.getForm().setFromPage(fromPage);
        page.getForm().setToPage(fromPage);
        setFormAttributes(page, model);
        return POST_REPLY_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_POST', 'EDIT_OWN_POST', 'ADMIN')")
    @GetMapping(Routes.POST_EDIT)
    public String edit(@PathVariable int id, @RequestParam(name = BaseController.ATTR_PAGE, required = false, defaultValue = "1") int fromPage, Model model) {
        PostForm form = getFormFromModel(model, PostForm::new);
        FormPageDto<? extends BaseDto, PostForm> page = postService.getEditForm(id, form);
        page.getForm().setFromPage(fromPage);
        page.getForm().setToPage(fromPage);
        setFormAttributes(page, model);
        return POST_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('CREATE_POST', 'ADMIN')")
    @PostMapping(Routes.POST_SAVE_REPLY)
    public String addSave(@ModelAttribute(BaseController.ATTR_FORM) @Valid PostForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, postService::create);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_POST', 'EDIT_OWN_POST', 'ADMIN')")
    @PostMapping(Routes.POST_SAVE)
    public String editSave(@ModelAttribute(BaseController.ATTR_FORM) @Valid PostForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, postService::edit);
    }

    protected void setFormUrls(BaseForm baseForm) {
        PostForm form = (PostForm)baseForm;

        // Add post
        if(form.getId() == 0) {
            // Reply
            if(form.getIdParent() != null) {
                form.setFormUrl(buildSegmentsQueryPath(Routes.POST_REPLY, Routes.PAGE_QUERY, Map.of(BaseController.ATTR_ID, form.getIdParent(), BaseController.ATTR_PAGE, form.getFromPage())));
            }
            // Add from topics list
            else {
                form.setFormUrl(buildSegmentsQueryPath(Routes.TOPIC_VIEW, Routes.PAGE_QUERY, Map.of(BaseController.ATTR_ID, form.getIdTopic(), BaseController.ATTR_PAGE, form.getFromPage())));
            }
            form.setSaveUrl(Routes.POST_SAVE_REPLY);
        }
        // Edit post
        else {
            form.setFormUrl(buildSegmentsQueryPath(Routes.POST_EDIT, Routes.PAGE_QUERY, Map.of(BaseController.ATTR_ID, form.getId(), BaseController.ATTR_PAGE, form.getFromPage())));
            form.setSaveUrl(Routes.POST_SAVE);
        }
        form.setCancelUrl(buildSegmentsQueryPath(Routes.TOPIC_VIEW, Routes.PAGE_QUERY, Map.of(BaseController.ATTR_ID, form.getIdTopic(), BaseController.ATTR_PAGE, form.getFromPage())));
        form.setFinishUrl(buildSegmentsQueryPath(Routes.TOPIC_VIEW, Routes.PAGE_QUERY, Map.of(BaseController.ATTR_ID, form.getIdTopic(), BaseController.ATTR_PAGE, form.getToPage())));
    }

    @PreAuthorize("hasAnyPermissions('CREATE_POST', 'ADMIN')")
    @GetMapping(Routes.POST_SAVE_REPLY)
    public String redirectReply(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('EDIT_POST', 'EDIT_OWN_POST', 'ADMIN')")
    @GetMapping(Routes.POST_SAVE)
    public String redirectEdit(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('DELETE_POST', 'ADMIN')")
    @GetMapping(Routes.POST_DELETE)
    public String delete(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(Routes.TOPIC_VIEW, page, postService.delete(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_POST', 'ACTIVATE_POST', 'ADMIN')")
    @GetMapping(Routes.POST_CHANGE_ACTIVE)
    public String changeActive(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(Routes.TOPIC_VIEW, page, postService.changeActive(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_POST', 'MAKE_STICKY_POST', 'ADMIN') || hasPermission('MAKE_STICKY_OWN_POST') && hasPermission('EDIT_OWN_POST')")
    @GetMapping(Routes.POST_CHANGE_STICKY)
    public String changeSticky(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(Routes.TOPIC_VIEW, page, postService.changeSticky(id));
    }

    @PreAuthorize("hasAnyPermissions('APPROVE_POST', 'ADMIN')")
    @GetMapping(Routes.POST_APPROVE)
    public String approve(@PathVariable int id, @RequestParam(required = false) Integer page) {
        return "redirect:" + makeLinkForRedirect(Routes.TOPIC_VIEW, page, postService.approve(id));
    }

    @PreAuthorize("hasPermission('CREATE_ATTACHMENTS') && (hasPermission('EDIT_OWN_POST') || hasPermission('EDIT_POST')) || hasPermission('ADMIN')")
    @GetMapping(Routes.POST_REMOVE_ATTACHMENT)
    public String removeAttachment(@PathVariable int id, @PathVariable int aid, @RequestParam(required = false) Integer page) {
        return makeLinkForRedirect(Routes.TOPIC_EDIT, page, postService.removeAttachment(id, aid));
    }

    private String makeLinkForRedirect(String address, Integer page, ActionStatusDto<Post> actionStatus) {
        if(actionStatus == null || actionStatus.getEntity() == null || actionStatus.getEntity().getTopic() == null) {
            return Routes.ROOT_URL;
        }
        if(page != null) {
            Map<String, Object> uriParams = Map.of(BaseController.ATTR_ID, actionStatus.getEntity().getTopic().getId(), BaseController.ATTR_PAGE, page);
            return buildSegmentsQueryPath(address, Routes.PAGE_QUERY, uriParams);
        }
        else {
            return buildSegmentsPath(address, actionStatus.getEntity().getTopic().getId());
        }
    }
}