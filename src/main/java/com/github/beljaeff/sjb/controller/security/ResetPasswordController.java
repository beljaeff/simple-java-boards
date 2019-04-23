package com.github.beljaeff.sjb.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordForm;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordRequestForm;
import com.github.beljaeff.sjb.dto.form.security.SecretQuestionForm;
import com.github.beljaeff.sjb.exception.UserAnswerException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.exception.UserTokenNotValidException;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.service.EmailService;
import com.github.beljaeff.sjb.service.security.ResetPasswordService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_ERROR;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_QUESTION;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_REQUEST;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_REQUEST_SUCCESS;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_RESET;
import static com.github.beljaeff.sjb.controller.Routes.RESET_PASSWORD_RESET_SUCCESS;
import static com.github.beljaeff.sjb.util.HttpUtils.getBasePath;
import static com.github.beljaeff.sjb.util.Utils.addError;

@PreAuthorize("isAnonymous()")
@Controller
public class ResetPasswordController extends BaseController {

    private static final String REQUEST_TPL         = "reset-password/request";
    private static final String ERROR_TPL           = "reset-password/error";
    private static final String QUESTION_TPL        = "reset-password/question";
    private static final String REQUEST_SUCCESS_TPL = "reset-password/request-success";
    private static final String RESET_TPL           = "reset-password/reset";
    private static final String RESET_SUCCESS_TPL   = "reset-password/reset-success";

    private static final String ATTR_REQUEST_FORM   = "resetPasswordRequestForm";
    private static final String ATTR_QUESTION_FORM  = "secretQuestionForm";
    private static final String ATTR_NICKNAME       = "nickName";
    private static final String ATTR_QUESTION       = "secretQuestion";
    private static final String ATTR_ANSWER         = "secretAnswer";
    private static final String ATTR_ANSWER_TRIES   = "answerTries";
    private static final String ATTR_ERRORS         = "resetPasswordErrors";

    private final ResetPasswordService resetPasswordService;
    private final EmailService emailService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService, EmailService emailService) {
        this.resetPasswordService = resetPasswordService;
        this.emailService = emailService;
    }

    private void setBreadcrumbs(Model model) {
        List<BreadcrumbDto> breadcrumbs = getBreadcrumbs();
        breadcrumbs.add(new BreadcrumbDto(RESET_PASSWORD, recordService.getText("reset.password.title")));
        model.addAttribute(ATTR_BREADCRUMBS, breadcrumbs);
    }

    private List<String> handleUserTokenNotValidException(UserTokenNotValidException e) {
        List<String> result = new ArrayList<>();
        if(e.getIsBadToken()) {
            result.add(recordService.getText("reset.password.token.empty"));
        }
        return result;
    }

    private List<String> handleUserCheckException(UserCheckException e, boolean isCheckBefore) {
        List<String> result = new ArrayList<>();
        if(e.getIsNull()) {
            result.add(recordService.getText("reset.password.not.found"));
        }
        else {
            if(!e.getIsActivated()) {
                result.add(recordService.getText("reset.password.not.activated"));
            }
            if(!e.getIsActive()) {
                result.add(recordService.getText("reset.password.not.active"));
            }
            if(e.getIsBanned()) {
                result.add(recordService.getText("reset.password.banned"));
            }
            if(e.getIsAfter() && !isCheckBefore || e.getIsBefore() && isCheckBefore) {
                result.add(recordService.getText("reset.password.time.exceeded"));
            }
        }
        return result;
    }

    private String sendVerificationEmail(User user, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ATTR_NICKNAME, user.getNickName());
        emailService.createAndSendResetPasswordMessage(user.getEmail(), user.getValidationCode(), getBasePath());
        return "redirect:" + RESET_PASSWORD_REQUEST_SUCCESS;
    }

    @GetMapping(RESET_PASSWORD)
    public String entryPoint() {
        return "forward:" + RESET_PASSWORD_REQUEST;
    }

    @GetMapping(RESET_PASSWORD_REQUEST)
    public String displayRequestForm(Model model, ResetPasswordRequestForm resetPasswordRequestForm) {
        model.addAttribute(ATTR_REQUEST_FORM, resetPasswordRequestForm);
        setBreadcrumbs(model);
        return REQUEST_TPL;
    }

    @PostMapping(RESET_PASSWORD_REQUEST)
    public String handleRequestForm(@Valid ResetPasswordRequestForm resetPasswordRequestForm,
                                    BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // Handle input and get user account info
        User user = null;
        if(!bindingResult.hasErrors()) {
            try {
                user = resetPasswordService.requestUserToResetPassword(resetPasswordRequestForm);
            }
            catch (UserCheckException e) {
                redirectAttributes.addFlashAttribute(ATTR_ERRORS, handleUserCheckException(e, true));
                return "redirect:" + RESET_PASSWORD_ERROR;
            }
        }
        if(user == null || bindingResult.hasErrors()) {
            return displayRequestForm(model, resetPasswordRequestForm);
        }

        // Ask secret question if we can
        if(!isEmpty(user.getSecretAnswer()) && !isEmpty(user.getSecretQuestion())) {
            // PRG pattern
            SecretQuestionForm secretQuestionForm = new SecretQuestionForm();
            secretQuestionForm.setNickName(user.getNickName());
            redirectAttributes.addFlashAttribute(secretQuestionForm);
            redirectAttributes.addFlashAttribute(ATTR_QUESTION, user.getSecretQuestion());
            redirectAttributes.addFlashAttribute(ATTR_ANSWER_TRIES, user.getSecretAnswerTries());
            return "redirect:" + RESET_PASSWORD_QUESTION;
        }

        return sendVerificationEmail(user, redirectAttributes);
    }

    @GetMapping(RESET_PASSWORD_QUESTION)
    public String showQuestionForm(Model model) {
        Object form = model.asMap().get(ATTR_QUESTION_FORM);
        if(form != null) {
            model.addAttribute(ATTR_QUESTION_FORM, form);
            model.addAttribute(ATTR_QUESTION, model.asMap().get(ATTR_QUESTION));
            model.addAttribute(ATTR_ANSWER_TRIES, model.asMap().get(ATTR_ANSWER_TRIES));
            setBreadcrumbs(model);
            return QUESTION_TPL;
        }
        return "redirect:" + RESET_PASSWORD_REQUEST;
    }

    @PostMapping(RESET_PASSWORD_QUESTION)
    public String handleQuestionForm(@Valid SecretQuestionForm secretQuestionForm,
                                    BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        User user = null;
        if(!bindingResult.hasErrors()) {
            try {
                user = resetPasswordService.getUserForSecretAnswer(secretQuestionForm);
                model.addAttribute(ATTR_QUESTION, user.getSecretQuestion());
                model.addAttribute(ATTR_ANSWER_TRIES, user.getSecretAnswerTries());
            }
            catch (UserCheckException e) {
                redirectAttributes.addFlashAttribute(ATTR_ERRORS, handleUserCheckException(e, false));
                return "redirect:" + RESET_PASSWORD_ERROR;
            }
            catch (UserAnswerException e) {
                if(!e.getIsAnswerCorrect()) {
                    addError(bindingResult, ATTR_ANSWER, recordService.getText("reset.password.secret.question.answer.incorrect"));
                }
                if(e.getAnswerTriesLeft() == 0) {
                    addError(bindingResult, ATTR_ANSWER, recordService.getText("reset.password.secret.question.tries.is.over"));
                }
                model.addAttribute(ATTR_QUESTION, e.getSecretQuestion());
                model.addAttribute(ATTR_ANSWER_TRIES, e.getAnswerTries());
            }
        }
        if(user == null || bindingResult.hasErrors()) {
            setBreadcrumbs(model);
            return QUESTION_TPL;
        }

        return sendVerificationEmail(user, redirectAttributes);
    }

    @GetMapping(RESET_PASSWORD_REQUEST_SUCCESS)
    public String requestSuccess(Model model) {
        Object nickName = model.asMap().get(ATTR_NICKNAME);
        if(nickName != null) {
            model.addAttribute(ATTR_NICKNAME, nickName);
            setBreadcrumbs(model);
            return REQUEST_SUCCESS_TPL;
        }
        return "redirect:" + RESET_PASSWORD_REQUEST;
    }

    @GetMapping(RESET_PASSWORD_RESET)
    public String displayResetPasswordForm(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        try {
            if(isEmpty(token)) {
                throw new UserTokenNotValidException();
            }
            resetPasswordService.checkUserForPasswordReset(token);
            ResetPasswordForm form = new ResetPasswordForm();
            form.setValidationCode(token);
            model.addAttribute(form);
            setBreadcrumbs(model);

            return RESET_TPL;
        }
        catch (UserTokenNotValidException e) {
            redirectAttributes.addFlashAttribute(ATTR_ERRORS, handleUserTokenNotValidException(e));
            return "redirect:" + RESET_PASSWORD_ERROR;
        }
        catch (UserCheckException e) {
            redirectAttributes.addFlashAttribute(ATTR_ERRORS, handleUserCheckException(e, false));
            return "redirect:" + RESET_PASSWORD_ERROR;
        }
    }

    @PostMapping(RESET_PASSWORD_RESET)
    public String resetPassword(@Valid ResetPasswordForm resetPasswordForm,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // Handle input and get user account info
        User user = null;
        if(!bindingResult.hasErrors()) {
            try {
                user = resetPasswordService.resetPassword(resetPasswordForm);
            }
            catch (UserCheckException e) {
                redirectAttributes.addFlashAttribute(ATTR_ERRORS, handleUserCheckException(e, false));
                return "redirect:" + RESET_PASSWORD_ERROR;
            }
        }
        if(user == null || bindingResult.hasErrors()) {
            setBreadcrumbs(model);
            return RESET_TPL;
        }

        emailService.createAndSendResetPasswordSuccessMessage(user.getEmail(), getBasePath());

        redirectAttributes.addFlashAttribute(ATTR_NICKNAME, user.getNickName());
        return "redirect:" + RESET_PASSWORD_RESET_SUCCESS;
    }

    @GetMapping(RESET_PASSWORD_RESET_SUCCESS)
    public String resetSuccess(Model model) {
        Object nickName = model.asMap().get(ATTR_NICKNAME);
        if(nickName != null) {
            model.addAttribute(ATTR_NICKNAME, nickName);
            setBreadcrumbs(model);
            return RESET_SUCCESS_TPL;
        }
        return "redirect:" + RESET_PASSWORD_REQUEST;
    }

    @GetMapping(RESET_PASSWORD_ERROR)
    public String error(Model model) {
        Object errors = model.asMap().get(ATTR_ERRORS);
        if(errors != null) {
            model.addAttribute(ATTR_ERRORS, errors);
            setBreadcrumbs(model);
            return ERROR_TPL;
        }
        return "redirect:" + RESET_PASSWORD_REQUEST;
    }
}