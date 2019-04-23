package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.exception.CredentialsNotUniqueException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.repository.condition.UserCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.dto.form.security.SignUpForm;
import com.github.beljaeff.sjb.enums.BaseGroups;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public SignUpServiceImpl(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    private boolean isAfter(User user) {
        return LocalDateTime.now().minusHours(24).isAfter(user.getLastValidationRequestDate());
    }

    @Override
    @Transactional
    public String registerUser(SignUpForm signUpForm) throws CredentialsNotUniqueException {
        Assert.notNull(signUpForm, "sign up form is not set");

        UserCondition condition = UserCondition.builder()
                .nickName(signUpForm.getNickName())
                .email(signUpForm.getEmail().toLowerCase())
                .joinWithOr(true)
                .build();

        User check = userService.getByCondition(condition);
        if(check != null) {
            log.debug("Can not register user: credentials not unique for email '{}' and/or nickname '{}'", signUpForm.getEmail(), signUpForm.getNickName());
            throw new CredentialsNotUniqueException(check, signUpForm.getEmail(), signUpForm.getNickName());
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setNickName(signUpForm.getNickName());
        user.setEmail(signUpForm.getEmail().toLowerCase());
        user.setPassword(userService.encodePassword(signUpForm.getPassword()));
        user.setName(signUpForm.getName());
        user.setSurname(signUpForm.getSurname());
        user.setGender(signUpForm.getGender());
        user.setSecretAnswer("");
        user.setSecretQuestion("");
        user.setRegisteredDate(now);
        user.setValidationCode(UUID.randomUUID().toString());
        user.setLastValidationRequestDate(now);

        Group group = groupService.getByName(BaseGroups.REGISTERED.name());
        user.setGroups(Collections.singleton(group));

        userService.save(user);

        return user.getValidationCode();
    }

    @Override
    @Transactional
    public User activateUserAndGet(String token) throws UserCheckException {
        Assert.notNull(token, "token should be set");

        UserCondition condition = UserCondition.builder()
                .validationCode(token)
                .build();
        User user = userService.getByCondition(condition);
        if(user == null || user.getIsActivated() || isAfter(user)) {
            log.debug("Can not activate user account: user account is null or already activated or activation time period expired. User: {}", user);
            throw new UserCheckException(user);
        }

        user.setIsActivated(true);
        user.setIsActive(true);
        user.setValidationCode("");
        user.setLastValidationRequestDate(LocalDateTime.MIN);
        userService.save(user);

        return user;
    }
}