package com.github.beljaeff.sjb.exception;

import com.github.beljaeff.sjb.enums.ErrorCode;

import java.util.List;

public class ForbiddenException extends ExceptionWithCode {
    public ForbiddenException(List<ErrorCode> errors) {
        super(errors);
    }
    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException() {}
}