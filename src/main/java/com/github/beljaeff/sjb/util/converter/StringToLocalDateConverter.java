package com.github.beljaeff.sjb.util.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String date) {
        if(date != null) {
            try {
                return LocalDate.parse(date);
            }
            catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }
}