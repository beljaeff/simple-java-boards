package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.model.common.PositionedEntity;

public interface PositionableRepository<T extends PositionedEntity, S extends Condition> extends BaseRepository<T, S> {
    default ActionStatusDto<T> getForPositionChanging(int id) {
        return new ActionStatusDto<>(get(id), true);
    }

    T getPrevPosition(T entity);

    T getNextPosition(T entity);
}