package com.github.beljaeff.sjb.dto.dto.profile;

import com.github.beljaeff.sjb.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverviewDto extends ProfileDto {
    private String email;
    private String name;
    private String surname;
    private String birthDate;
    private Gender gender;
    private String location;
    private String site;
    private String signature;
    private boolean hideEmail;
    private boolean hideBirthdate;
    private boolean isActivated;
}