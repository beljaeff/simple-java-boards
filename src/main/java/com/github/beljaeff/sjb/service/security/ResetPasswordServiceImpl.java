package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.exception.UserAnswerException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.repository.condition.UserCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordForm;
import com.github.beljaeff.sjb.dto.form.security.ResetPasswordRequestForm;
import com.github.beljaeff.sjb.dto.form.security.SecretQuestionForm;
import com.github.beljaeff.sjb.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserService userService;

    @Value("${max.secret.answer.tries}")
    private Integer maxSecretAnswerTries;

    @Autowired
    public ResetPasswordServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private boolean isAfter(User user) {
        return LocalDateTime.now().minusHours(24).isAfter(user.getLastValidationRequestDate());
    }

    private boolean isBadUser(User user) {
        return user == null || !user.getIsActivated() || !user.getIsActive() || user.getIsBanned();
    }

    @Override
    @Transactional
    public User requestUserToResetPassword(ResetPasswordRequestForm resetPasswordRequestForm) throws UserCheckException {
        Assert.notNull(resetPasswordRequestForm, "reset password form should be set");

        UserCondition condition = UserCondition.builder()
                .nickName(resetPasswordRequestForm.getInputId())
                .email(resetPasswordRequestForm.getInputId().toLowerCase())
                .joinWithOr(true)
                .build();
        User user = userService.getByCondition(condition);
        if(isBadUser(user) || !isAfter(user)) {
            log.debug("Can not reset password for user {}. User not active, not activated, banned or validation request already performed during past 24 hours", user);
            throw new UserCheckException(user);
        }

        user.setLastValidationRequestDate(LocalDateTime.now());
        user.setSecretAnswerTries(0);
        user.setValidationCode(UUID.randomUUID().toString());
        userService.save(user);

        return user;
    }

    @Override
    @Transactional
    public User getUserForSecretAnswer(SecretQuestionForm secretQuestionForm) throws UserCheckException, UserAnswerException {
        Assert.notNull(secretQuestionForm, "secret question form should be set");

        UserCondition condition = UserCondition.builder()
                .nickName(secretQuestionForm.getNickName())
                .build();
        User user = userService.getByCondition(condition);
        if(isBadUser(user) || isAfter(user)) {
            log.debug("Can not check secret answer for user {}. User not active, not activated, banned or validation ticket elapsed", user);
            throw new UserCheckException(user);
        }
        if(user.getSecretAnswerTries() == maxSecretAnswerTries) {
            log.debug("Secret answer tries elapsed for user {}", user.getNickName());
            throw new UserAnswerException(
                    false,
                    user.getSecretAnswerTries(),
                    user.getSecretQuestion(),
                    maxSecretAnswerTries - user.getSecretAnswerTries());
        }

        user.setSecretAnswerTries(user.getSecretAnswerTries()+1);
        userService.save(user);

        if(!user.getSecretAnswer().equals(secretQuestionForm.getSecretAnswer())) {
            log.debug("Secret answer does not match for user {}", user.getNickName());
            throw new UserAnswerException(
                    secretQuestionForm.getSecretAnswer().equals(user.getSecretAnswer()),
                    user.getSecretAnswerTries(),
                    user.getSecretQuestion(),
                    maxSecretAnswerTries - user.getSecretAnswerTries());
        }

        return user;
    }

    @Override
    public User checkUserForPasswordReset(String token) throws UserCheckException {
        Assert.notNull(token, "token should be set");

        UserCondition condition = UserCondition.builder()
                .validationCode(token)
                .build();
        User user = userService.getByCondition(condition);
        if(isBadUser(user) || isAfter(user)) {
            log.debug("Can not verify user {} for password reset. User not active, not activated, banned or validation ticket elapsed", user);
            throw new UserCheckException(user);
        }
        return user;
    }

    @Override
    public User resetPassword(ResetPasswordForm resetPasswordForm) throws UserCheckException {
        Assert.notNull(resetPasswordForm, "reset password form should be set");

        UserCondition condition = UserCondition.builder()
                .validationCode(resetPasswordForm.getValidationCode())
                .build();
        User user = userService.getByCondition(condition);
        if(isBadUser(user) || isAfter(user)) {
            log.debug("Can bot reset password for user {}. User not active, not activated, banned or validation ticket elapsed", user);
            throw new UserCheckException(user);
        }
        user.setPassword(userService.encodePassword(resetPasswordForm.getPassword()));
        user.setValidationCode("");
        userService.save(user);

        return user;
    }
}