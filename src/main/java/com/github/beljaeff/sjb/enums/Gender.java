package com.github.beljaeff.sjb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Gender {
    UNDEFINED("Not set"),
    MALE("Male"),
    FEMALE("Female");

    private String text;

    //TODO: test
    public static Map<String, String> getValues() {
        Map<String, String> ret = new LinkedHashMap<>();
        for(Gender gender : Gender.values()) {
            ret.put(gender.name(), gender.getText());
        }
        return ret;
    }
}
