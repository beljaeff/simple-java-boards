package com.github.beljaeff.sjb.exception;

import com.github.beljaeff.sjb.enums.ErrorCode;

import java.util.List;

public class NotFoundException extends ExceptionWithCode {
    public NotFoundException(List<ErrorCode> errors) {
        super(errors);
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException() {}
}