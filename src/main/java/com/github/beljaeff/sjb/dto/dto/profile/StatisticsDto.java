package com.github.beljaeff.sjb.dto.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsDto extends ProfileDto {
    private String registeredDate;
    private String lastLoginDate;
    private int topicsCount;
    private int postsCount;
    private int goodKarma;
    private int badKarma;
    private long timeLoggedIn;
}