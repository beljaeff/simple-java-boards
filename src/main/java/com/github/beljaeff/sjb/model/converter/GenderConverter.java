package com.github.beljaeff.sjb.model.converter;

import com.github.beljaeff.sjb.enums.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static com.github.beljaeff.sjb.enums.Gender.FEMALE;
import static com.github.beljaeff.sjb.enums.Gender.MALE;
import static com.github.beljaeff.sjb.enums.Gender.UNDEFINED;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(Gender gender) {
        return gender == null || gender == UNDEFINED ? null : gender == MALE;
    }

    @Override
    public Gender convertToEntityAttribute(Boolean dbGender) {
        return dbGender == null ? UNDEFINED : dbGender ? MALE : FEMALE;
    }
}
