package com.github.beljaeff.sjb.exception;

import lombok.Getter;
import com.github.beljaeff.sjb.enums.ErrorCode;

import java.util.List;

@Getter
public class UserProfileException extends ExceptionWithCode {
    public UserProfileException(List<ErrorCode> errors) {
        super(errors);
    }
}
