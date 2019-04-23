package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

import java.util.List;

public interface ListableRepository<T extends IdentifiedActiveEntity, S extends Condition> extends BaseRepository<T, S> {
    List<T> getList(S condition, String entityGraphName);
}