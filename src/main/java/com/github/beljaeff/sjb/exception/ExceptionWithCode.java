package com.github.beljaeff.sjb.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.github.beljaeff.sjb.enums.ErrorCode;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
abstract public class ExceptionWithCode extends RuntimeException {
    private List<ErrorCode> errors;

    public ExceptionWithCode(String message) {
        super(message);
    }

    public ExceptionWithCode(Throwable cause, List<ErrorCode> errors) {
        super(cause);
        this.errors = errors;
    }
}