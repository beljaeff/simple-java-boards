package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileSection {
    OVERVIEW   (Constants.OVERVIEW),
    SECURITY   (Constants.SECURITY),
    GROUPS     (Constants.GROUPS),
    POSTS      (Constants.POSTS),
    TOPICS     (Constants.TOPICS),
    STATISTICS (Constants.STATISTICS);

    private String code;

    public static class Constants {
        public static final String OVERVIEW   = "overview";
        public static final String SECURITY   = "security";
        public static final String GROUPS     = "groups";
        public static final String POSTS      = "posts";
        public static final String TOPICS     = "topics";
        public static final String STATISTICS = "statistics";
    }
}
