package com.github.beljaeff.sjb.exception;

import com.github.beljaeff.sjb.enums.ErrorCode;

import java.util.List;

public class PersistenceException extends ExceptionWithCode {
    public PersistenceException(Throwable cause, List<ErrorCode> errors) {
        super(cause, errors);
    }
}