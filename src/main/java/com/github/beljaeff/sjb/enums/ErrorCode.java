package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    PERSIST_ERROR("persist.error"),
    CATEGORY_ENTITY_NOT_FOUND("category.entity.not.found"),
    BOARD_ENTITY_NOT_FOUND("board.entity.not.found"),
    TOPIC_ENTITY_NOT_FOUND("topic.entity.not.found"),
    POST_ENTITY_NOT_FOUND("post.entity.not.found"),
    PM_ENTITY_NOT_FOUND("private_message.entity.not.found"),
    USER_ENTITY_NOT_FOUND("profile.entity.not.found"),
    ATTACHMENT_ENTITY_NOT_FOUND("attachment.entity.not.found"),
    PERMISSION_ENTITY_NOT_FOUND("permission.entity.not.found"),
    GROUP_ENTITY_NOT_FOUND("group.entity.not.found"),
    EMPTY_ENTITY_NOT_FOUND("empty.entity.not.found"),

    USER_NOT_FOUND("add.group.to.user.user.not.found"),
    GROUP_NOT_FOUND("add.group.to.user.group.not.found"),
    GROUP_DUPLICATE("add.group.to.user.group.duplicates"),

    PROFILE_NOT_FOUND("profile.not.found"),

    ACCESS_TO_SECURITY_SECTION_NOT_ALLOWED("access.to.security.section.not.allowed"),

    EMAIL_DUPLICATE("change.email.email.duplicate", "email"),
    PASSWORD_NOT_MATCH("change.email.password.not.match", "currentPassword"),

    AVATAR_NOT_SET("edit.avatar.avatar.not.set"),
    AVATAR_NOT_IMAGE("edit.avatar.avatar.not.image");

    ErrorCode(String code) {
        this.code = code;
    }

    private String code;
    private String field;

    public static ErrorCode getNotFoundByType(EntityType entityType) {
        for(ErrorCode code : ErrorCode.values()) {
            if(code.getCode().equals(entityType.getType() + ".entity.not.found")) {
                return code;
            }
        }
        return EMPTY_ENTITY_NOT_FOUND;
    }
}
