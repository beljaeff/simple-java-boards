package com.github.beljaeff.sjb.model.converter;

import lombok.extern.slf4j.Slf4j;
import com.github.beljaeff.sjb.enums.EntityType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class EntityTypeConverter implements AttributeConverter<EntityType, String> {

    @Override
    public String convertToDatabaseColumn(EntityType type) {
        if(type == null) {
            log.error("Error converting EntityType to String: entityType is null");
            throw new IllegalArgumentException("type should be not null");
        }
        return type.getType();
    }

    @Override
    public EntityType convertToEntityAttribute(String type) {
        if(type == null) {
            log.error("Error converting String to EntityType: string is null");
            throw new IllegalArgumentException("type should be not null");
        }
        for(EntityType t : EntityType.values()) {
            if(t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }
}
