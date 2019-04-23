package com.github.beljaeff.sjb.controller.security;

import com.github.beljaeff.sjb.controller.Routes;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.dto.form.security.SignUpForm;
import com.github.beljaeff.sjb.enums.Gender;
import com.github.beljaeff.sjb.exception.CredentialsNotUniqueException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.exception.UserTokenNotValidException;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.service.EmailService;
import com.github.beljaeff.sjb.service.security.SignUpService;
import com.github.beljaeff.sjb.util.HttpUtils;
import com.github.beljaeff.sjb.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("isAnonymous()")
@Controller
public class SignUpController extends BaseController {

    private static final String SIGN_UP_REQUEST_TPL         = "sign-up/request";
    private static final String SIGN_UP_REQUEST_SUCCESS_TPL = "sign-up/request-success";
    private static final String SIGN_UP_ACTIVATE_TPL        = "sign-up/activate";

    private static final String ATTR_FORM        = "signUpForm";
    private static final String ATTR_GENDER_LIST = "genderList";
    private static final String ATTR_EMAIL       = "email";
    private static final String ATTR_NICKNAME    = "nickName";
    private static final String ATTR_ERROR       = "activationError";

    private final SignUpService signUpService;
    private final EmailService emailService;

    @Autowired
    public SignUpController(SignUpService signUpService, EmailService emailService) {
        this.signUpService = signUpService;
        this.emailService = emailService;
    }

    private void setBreadcrumbs(Model model) {
        List<BreadcrumbDto> breadcrumbs = super.getBreadcrumbs();
        breadcrumbs.add(new BreadcrumbDto(Routes.SIGN_UP, recordService.getText("sign.up.title")));
        model.addAttribute(ATTR_BREADCRUMBS, breadcrumbs);
    }

    private String registerUser(SignUpForm signUpForm, BindingResult bindingResult) {
        try {
            return signUpService.registerUser(signUpForm);
        }
        catch (CredentialsNotUniqueException e) {
            // Set "not unique" errors to display inside sign up form
            if(!StringUtils.isEmpty(e.getEmail())) {
                Utils.addError(bindingResult, ATTR_EMAIL, recordService.getText("sign.up.form.email.not.unique"));
            }
            if(!StringUtils.isEmpty(e.getNickName())) {
                Utils.addError(bindingResult, ATTR_NICKNAME, recordService.getText("sign.up.form.nick.name.not.unique"));
            }
            return null;
        }
    }

    @GetMapping(Routes.SIGN_UP)
    public String entryPoint() {
        return "redirect:" + Routes.SIGN_UP_REQUEST;
    }

    @GetMapping(Routes.SIGN_UP_REQUEST)
    public String displaySignUpForm(Model model, SignUpForm signUpForm) {
        model.addAttribute(ATTR_FORM, signUpForm);
        model.addAttribute(ATTR_GENDER_LIST, Gender.getValues());
        setBreadcrumbs(model);
        return SIGN_UP_REQUEST_TPL;
    }

    @PostMapping(Routes.SIGN_UP_REQUEST)
    public String handleRequest(@Valid SignUpForm signUpForm,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // Handle input and create user account
        String token = null;
        if(!bindingResult.hasErrors()) {
            token = registerUser(signUpForm, bindingResult);
        }
        if(bindingResult.hasErrors()) {
            return displaySignUpForm(model, signUpForm);
        }

        emailService.createAndSendActivationMessage(signUpForm.getEmail(), token, HttpUtils.getBasePath());

        // PRG pattern
        redirectAttributes.addFlashAttribute(signUpForm);
        return "redirect:" + Routes.SIGN_UP_REQUEST_SUCCESS;
    }

    @GetMapping(Routes.SIGN_UP_REQUEST_SUCCESS)
    public String requestSuccess(Model model) {
        SignUpForm form = (SignUpForm) model.asMap().get(ATTR_FORM);
        if(form != null) {
            model.addAttribute(ATTR_NICKNAME, form.getNickName());
            model.addAttribute(ATTR_EMAIL, form.getEmail());
            setBreadcrumbs(model);
            return SIGN_UP_REQUEST_SUCCESS_TPL;
        }
        return "redirect:" + Routes.SIGN_UP_REQUEST;
    }

    @GetMapping(Routes.SIGN_UP_ACTIVATE)
    public String activate(@RequestParam String token, Model model) {
        try {
            if(StringUtils.isEmpty(token)) {
                throw new UserTokenNotValidException();
            }
            User user = signUpService.activateUserAndGet(token);
            model.addAttribute(ATTR_NICKNAME, user.getNickName());
            setBreadcrumbs(model);

            emailService.createAndSendUserRegisteredMessage(user.getEmail(), HttpUtils.getBasePath());
        }
        catch (UserTokenNotValidException | UserCheckException e) {
            model.addAttribute(ATTR_ERROR, e);
        }
        return SIGN_UP_ACTIVATE_TPL;
    }
}