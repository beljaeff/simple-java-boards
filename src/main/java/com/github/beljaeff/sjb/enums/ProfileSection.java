package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileSection {
    OVERVIEW   (ConstantsHelper.OVERVIEW),
    SECURITY   (ConstantsHelper.SECURITY),
    GROUPS     (ConstantsHelper.GROUPS),
    POSTS      (ConstantsHelper.POSTS),
    TOPICS     (ConstantsHelper.TOPICS),
    STATISTICS (ConstantsHelper.STATISTICS);

    private final String code;

    public static class ConstantsHelper {
        public static final String OVERVIEW   = "overview";
        public static final String SECURITY   = "security";
        public static final String GROUPS     = "groups";
        public static final String POSTS      = "posts";
        public static final String TOPICS     = "topics";
        public static final String STATISTICS = "statistics";
    }
}
