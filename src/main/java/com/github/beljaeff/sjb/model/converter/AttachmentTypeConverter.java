package com.github.beljaeff.sjb.model.converter;

import lombok.extern.slf4j.Slf4j;
import com.github.beljaeff.sjb.enums.AttachmentType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class AttachmentTypeConverter implements AttributeConverter<AttachmentType, String> {

    @Override
    public String convertToDatabaseColumn(AttachmentType type) {
        if(type == null) {
            log.error("Error converting AttachmentType to String: attachmentType is null");
            throw new IllegalArgumentException("type should be not null");
        }
        return type.getType();
    }

    @Override
    public AttachmentType convertToEntityAttribute(String type) {
        if(type == null) {
            log.error("Error converting String to AttachmentType: string is null");
            throw new IllegalArgumentException("type should be not null");
        }
        for(AttachmentType t : AttachmentType.values()) {
            if(t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }
}