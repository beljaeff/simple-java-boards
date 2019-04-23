package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    CATEGORY(Constants.CATEGORY),
    BOARD(Constants.BOARD),
    TOPIC(Constants.TOPIC),
    POST(Constants.POST),
    PM(Constants.PM),
    PROFILE(Constants.PROFILE),
    ATTACHMENT(Constants.ATTACHMENT),
    GROUP(Constants.GROUP),
    PERMISSION(Constants.PERMISSION),
    EMPTY(Constants.EMPTY);

    private String type;

    public static EntityType getByName(String entityName) {
        for(EntityType entityType : EntityType.values()) {
            if(entityType.getType().equals(entityName)) {
                return entityType;
            }
        }
        return EMPTY;
    }

    public static class Constants {
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