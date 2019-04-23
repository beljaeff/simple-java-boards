package com.github.beljaeff.sjb.util.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class LocalDateToStringConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(LocalDate date) {
        if(date != null) {
            return date.toString();
        }
        return "";
    }
}