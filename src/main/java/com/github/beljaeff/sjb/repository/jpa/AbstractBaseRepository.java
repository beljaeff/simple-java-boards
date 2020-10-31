package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.Condition;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import com.github.beljaeff.sjb.repository.BaseRepository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.beljaeff.sjb.model.EntityGraphNamesHelper.HINT_FETCH;

abstract public class AbstractBaseRepository<T extends IdentifiedActiveEntity, S extends Condition> implements BaseRepository<T, S> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Getter
    protected Class<T> entityClass;

    @Override
    public T get(int id, String entityGraphName) {
        if(entityGraphName != null) {
            EntityGraph entityGraph = entityManager.getEntityGraph(entityGraphName);
            Map<String, Object> hints = new HashMap<>();
            hints.put(HINT_FETCH, entityGraph);
            return entityManager.find(entityClass, id, hints);
        }
        return entityManager.find(entityClass, id);
    }

    @Override
    public T get(int id) {
        return get(id, null);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        T object = entityManager.find(entityClass, id);
        if(object == null) {
            return false;
        }
        entityManager.remove(object);
        return entityManager.find(entityClass, id) == null;
    }

    @Override
    public boolean delete(T object) {
        return delete(object.getId());
    }

    @Transactional
    @Override
    public void add(@Valid T object) {
        entityManager.persist(object);
    }

    @Transactional
    @Override
    public T update(@Valid T object) {
        return entityManager.merge(object);
    }

    /**
     * Checks is there any inactive entity in sequence starting from given entityId. Sequence is post -> topic -> board -> category.
     * @param entityId - id category, board, topic, post, user, group or attachment
     * @param entityType - entity type
     * @param checkParents - when false checks isActive only for given entity determined by entityId, otherwise checks all
     *                     entity sequence
     *
     * @return true if there is no inactive entities found, false otherwise
     */
    protected boolean isEntityActive(Integer entityId, EntityType entityType, Boolean checkParents) {
        return (boolean)
            entityManager
                .createNamedStoredProcedureQuery(IdentifiedActiveEntity.PROC_IS_ENTITY_ACTIVE)
                .setParameter("entity_id", entityId)
                .setParameter("entity_type", entityType.getType())
                .setParameter("check_parents", checkParents)
                .getSingleResult();
    }

    /**
     * Returns entities sequence starting from entity represented by entityId. Sequence example:
     * post -> topic -> board -> board -> board -> category. Entity can be category, board, topic, post, user, group or attachment.
     * If top-level board have id_category is null then fake empty category is used.
     * @param entityId to start from
     * @param entityType - entity type object
     * @return resulting list of CommonEntity objects, ordered by id (just counter, descending order from root to leaf),
     * where type field can be 'post', 'topic', 'board' or 'category', ordered by id.
     */
    protected List<CommonEntity> getBreadcrumbs(Integer entityId, EntityType entityType) {
        return entityManager
            .createNamedQuery(CommonEntity.PROC_GET_BREADCRUMBS, CommonEntity.class)
            .setParameter("entityId", entityId)
            .setParameter("entityType", entityType.getType())
            .getResultList();
    }

    /**
     * Default implementation
     */
    @Override
    public List<CommonEntity> getBreadcrumbs(Integer entityId) {
        return Collections.emptyList();
    }
}