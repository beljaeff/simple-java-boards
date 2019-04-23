package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

public interface PositionableService<T extends IdentifiedActiveEntity> extends CrudService<T> {
    ActionStatusDto<T> up(int id);

    ActionStatusDto<T> down(int id);
}