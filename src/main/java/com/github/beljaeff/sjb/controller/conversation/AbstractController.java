package com.github.beljaeff.sjb.controller.conversation;

import com.github.beljaeff.sjb.controller.Routes;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.exception.PersistenceException;
import com.github.beljaeff.sjb.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.form.BaseForm;

import java.util.function.Consumer;
import java.util.function.Supplier;

abstract public class AbstractController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    void setFormAttributes(FormPageDto<? extends BaseDto, ? extends BaseForm> page, Model model) {
        setFormUrls(page.getForm());
        model.addAttribute(ATTR_PAGE, page);
        model.addAttribute(ATTR_FORM, page.getForm());
        model.addAttribute(ATTR_ACTIVE_LIST, Utils.getActiveValues(recordService));
    }

    abstract void setFormUrls(BaseForm form);

    <T extends BaseForm> String saveForm(T form, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                         Consumer<T> saveMethodReference) {
        if(!bindingResult.hasErrors()) {
            try {
                saveMethodReference.accept(form);
            }
            catch (PersistenceException | NotFoundException e) {
                log.warn("Entity {} not saved", form);
                log.warn(e.getMessage(), e);
                Utils.addErrors(bindingResult, recordService, e.getErrors());
            }
        }

        setFormUrls(form);

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(ATTR_BINDING_RESULT, bindingResult);
            redirectAttributes.addFlashAttribute(ATTR_FORM, form);
            return "redirect:" + form.getFormUrl();
        }

        // PRG pattern
        redirectAttributes.addFlashAttribute(ATTR_FORM, form);
        return "redirect:" + form.getSaveUrl();
    }

    String redirect(Model model) {
        BaseForm form = (BaseForm) model.asMap().get(ATTR_FORM);
        if(form != null && form.getFinishUrl() != null) {
            return "redirect:" + form.getFinishUrl();
        }
        return "redirect:" + Routes.ROOT_URL;
    }

    @SuppressWarnings("unchecked")
    <T extends BaseForm> T getFormFromModel(final Model model, Supplier<T> constructorReference) {
        return model.containsAttribute(ATTR_FORM) ? (T) model.asMap().get(ATTR_FORM) : constructorReference.get();
    }
}