package com.github.beljaeff.sjb.dto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDto {
    private int id;
    private boolean isActive;
}