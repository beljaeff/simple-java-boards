package com.github.beljaeff.sjb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenNotValidException extends Exception {
    private boolean isBadToken;

    public UserTokenNotValidException() {
        isBadToken = true;
    }
}