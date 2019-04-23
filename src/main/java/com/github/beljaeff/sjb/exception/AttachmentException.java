package com.github.beljaeff.sjb.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AttachmentException extends RuntimeException {
    public AttachmentException(Throwable cause) {
        super(cause);
    }
}
