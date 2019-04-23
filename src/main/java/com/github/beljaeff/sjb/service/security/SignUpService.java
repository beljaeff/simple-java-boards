package com.github.beljaeff.sjb.service.security;

import com.github.beljaeff.sjb.exception.CredentialsNotUniqueException;
import com.github.beljaeff.sjb.exception.UserCheckException;
import com.github.beljaeff.sjb.dto.form.security.SignUpForm;
import com.github.beljaeff.sjb.model.User;

public interface SignUpService {
    String registerUser(SignUpForm form) throws CredentialsNotUniqueException;
    User activateUserAndGet(String token) throws UserCheckException;
}
