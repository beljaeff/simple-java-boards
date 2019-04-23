package com.github.beljaeff.sjb.controller.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.github.beljaeff.sjb.controller.Routes.SIGN_IN;

@PreAuthorize("isAnonymous()")
@Controller
public class SignInController {

    private static final String SIGN_IN_TPL = "sign-in";

    @GetMapping(SIGN_IN)
    public String displaySignInForm() {
        return SIGN_IN_TPL;
    }
}