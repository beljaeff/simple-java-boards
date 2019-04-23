package com.github.beljaeff.sjb.dto.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityDto extends ProfileDto {
    private String secretQuestion;
    private String secretAnswer;
}
