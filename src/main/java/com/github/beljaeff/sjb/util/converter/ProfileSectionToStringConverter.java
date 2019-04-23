package com.github.beljaeff.sjb.util.converter;

import com.github.beljaeff.sjb.enums.ProfileSection;
import org.springframework.core.convert.converter.Converter;

public class ProfileSectionToStringConverter implements Converter<ProfileSection, String> {
    @Override
    public String convert(ProfileSection section) {
        if(section != null) {
            return section.getCode();
        }
        return null;
    }
}