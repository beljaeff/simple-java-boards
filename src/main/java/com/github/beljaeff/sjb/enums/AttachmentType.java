package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachmentType {
    AVATAR("avatar"), POST("post"), PRIVATE_MESSAGE("private-message");

    private String type;
}