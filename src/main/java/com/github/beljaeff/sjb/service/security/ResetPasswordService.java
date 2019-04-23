package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.exception.UserAnswerException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordForm;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordRequestForm;
import com.github.beljaeff.sjb.dto.form.security.SecretQuestionForm;
import com.github.beljaeff.sjb.model.User;

public interface ResetPasswordService {

    User requestUserToResetPassword(ResetPasswordRequestForm form) throws UserCheckException;

    User getUserForSecretAnswer(SecretQuestionForm secretQuestionForm) throws UserCheckException, UserAnswerException;

    User checkUserForPasswordReset(String token) throws UserCheckException;

    User resetPassword(ResetPasswordForm resetPasswordForm) throws UserCheckException;
}
