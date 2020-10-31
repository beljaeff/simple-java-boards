package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    CATEGORY(ConstantsHelper.CATEGORY),
    BOARD(ConstantsHelper.BOARD),
    TOPIC(ConstantsHelper.TOPIC),
    POST(ConstantsHelper.POST),
    PM(ConstantsHelper.PM),
    PROFILE(ConstantsHelper.PROFILE),
    ATTACHMENT(ConstantsHelper.ATTACHMENT),
    GROUP(ConstantsHelper.GROUP),
    PERMISSION(ConstantsHelper.PERMISSION),
    EMPTY(ConstantsHelper.EMPTY);

    private final String type;

    public static EntityType getByName(String entityName) {
        for(EntityType entityType : EntityType.values()) {
            if(entityType.getType().equals(entityName)) {
                return entityType;
            }
        }
        return EMPTY;
    }

    public static class ConstantsHelper {
        public static final String CATEGORY   = "category";
        public static final String BOARD      = "board";
        public static final String TOPIC      = "topic";
        public static final String POST       = "post";
        public static final String PM         = "private_message";
        public static final String PROFILE    = "profile";
        public static final String ATTACHMENT = "attachment";
        public static final String PERMISSION = "permission";
        public static final String GROUP      = "group";
        public static final String EMPTY      = "empty";
    }
}