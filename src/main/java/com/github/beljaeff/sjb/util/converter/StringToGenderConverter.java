package com.github.beljaeff.sjb.util.converter;

import com.github.beljaeff.sjb.enums.Gender;
import org.springframework.core.convert.converter.Converter;

public class StringToGenderConverter implements Converter<String, Gender> {
    @Override
    public Gender convert(String gender) {
        if(gender != null) {
            try {
                return Gender.valueOf(gender);
            }
            catch (IllegalArgumentException iae) {
                return null;
            }
        }
        return null;
    }
}