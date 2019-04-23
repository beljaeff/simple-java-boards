package com.github.beljaeff.sjb.util.converter;

import com.github.beljaeff.sjb.enums.ProfileSection;
import org.springframework.core.convert.converter.Converter;

public class StringToProfileSectionConverter implements Converter<String, ProfileSection> {
    @Override
    public ProfileSection convert(String strSection) {
        for(ProfileSection section : ProfileSection.values()) {
            if(section.getCode().equals(strSection)) {
                return section;
            }
        }
        return null;
    }
}