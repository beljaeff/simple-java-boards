package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

import java.util.List;

public interface BaseRepository<T extends IdentifiedActiveEntity, S extends Condition> {

    Class<T> getEntityClass();

    T get(int id, String entityGraphName);

    T get(int id);

    void add(T entity);

    T update(T entity);

    boolean delete(T entity);

    boolean delete(int id);

    /**
     * Checks is there any inactive entity in sequence starting from given entityId. Sequence example:
     * post -> topic -> board -> board -> board -> category. Entity can be category, board, topic, post, user, group or attachment.
     * @param entityId - id category, board, topic, post, user, group or attachment
     * @param checkParents - when false checks isActive only for given entity determined by entityId, otherwise checks all
     *                     entity sequence
     *
     * @return true if there is no inactive entities found, false otherwise
     */
    boolean isEntityActive(Integer entityId, Boolean checkParents);

    /**
     * Returns entities sequence starting from entity represented by entityId. Sequence example:
     * post -> topic -> board -> board -> board -> category. Entity can be category, board, topic, post, user, group or attachment.
     * If top-level board have id_category is null then fake empty category is used.
     * @param entityId to start from
     * @return resulting list of CommonEntity objects, ordered by id (just counter, descending order from root to leaf),
     * where type field can be 'post', 'topic', 'board' or 'category', ordered by id.
     */
    List<CommonEntity> getBreadcrumbs(Integer entityId);
}