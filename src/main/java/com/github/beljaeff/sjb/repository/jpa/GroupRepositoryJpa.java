package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.GroupCondition;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.Group_;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.model.User_;
import com.github.beljaeff.sjb.repository.GroupRepository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static com.github.beljaeff.sjb.model.EntityGraphs.GROUPS_WITH_OWNERS;
import static com.github.beljaeff.sjb.model.EntityGraphs.HINT_FETCH;

//TODO: tests
@Repository
public class GroupRepositoryJpa extends AbstractBaseRepository<Group, GroupCondition> implements GroupRepository {

    public GroupRepositoryJpa() {
        entityClass = Group.class;
    }

    /**
     * SELECT * FROM groups WHERE code={code} ORDER BY weight ASC, name ASC
     * @param condition - group condition, contains group code
     * @param entityGraphName - graph to apply
     */
    @Override
    public List<Group> getList(GroupCondition condition, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> fromGroup = criteriaQuery.from(Group.class);
        criteriaQuery.select(fromGroup);

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqual(fromGroup.get(Group_.code), condition.getCode())
                .build();
        if(resultCondition != null) {
            criteriaQuery.where(resultCondition);
        }

        Order weightAsc = criteriaBuilder.asc(fromGroup.get(Group_.weight));
        Order nameAsc   = criteriaBuilder.asc(fromGroup.get(Group_.name));
        criteriaQuery.orderBy(weightAsc, nameAsc);

        TypedQuery<Group> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        return typedQuery.getResultList();
    }

    @Override
    public List<Group> getListByUsers(List<Integer> userIds) {
        if(isEmpty(userIds)) {
            return Collections.emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> fromGroup = criteriaQuery.from(Group.class);
        Join<Group, User> leftJoinUser = fromGroup.join(Group_.owners, JoinType.LEFT);
        criteriaQuery.select(fromGroup);
        criteriaQuery.distinct(true);
        CriteriaBuilder.In<Integer> in = criteriaBuilder.in(leftJoinUser.get(User_.id));
        userIds.forEach(in::value);
        criteriaQuery.where(in);

        TypedQuery<Group> typedQuery = entityManager.createQuery(criteriaQuery);
        EntityGraph graph = entityManager.getEntityGraph(GROUPS_WITH_OWNERS);
        typedQuery.setHint(HINT_FETCH, graph);

        return typedQuery.getResultList();
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.GROUP, checkParents);
    }
}