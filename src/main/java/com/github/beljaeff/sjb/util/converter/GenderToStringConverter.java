package com.github.beljaeff.sjb.util.converter;

import com.github.beljaeff.sjb.enums.Gender;
import org.springframework.core.convert.converter.Converter;

public class GenderToStringConverter implements Converter<Gender, String> {
    @Override
    public String convert(Gender gender) {
        if(gender != null) {
            return gender.name();
        }
        return null;
    }
}