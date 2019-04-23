package com.github.beljaeff.sjb.dto.dto;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionStatusDto<T extends IdentifiedActiveEntity> {
    private T entity;
    private boolean status;
}