package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

import java.util.List;

public interface PageableRepository<T extends IdentifiedActiveEntity, S extends Condition> extends ListableRepository<T, S> {
    PagedEntityList<T> getPageableList(S condition, Integer page, String entityGraphName);

    default List<T> getList(S condition, String entityGraphName) {
        return getPageableList(condition, null, entityGraphName).getList();
    }
}
