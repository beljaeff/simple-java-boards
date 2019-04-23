package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.UserCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.model.User_;
import com.github.beljaeff.sjb.repository.GroupRepository;
import com.github.beljaeff.sjb.repository.UserRepository;

import javax.persistence.EntityGraph;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.beljaeff.sjb.model.EntityGraphs.HINT_FETCH;
import static com.github.beljaeff.sjb.util.UserUtils.ANONYMOUS_ID;
import static com.github.beljaeff.sjb.util.Utils.mapGroupsByUserId;

//TODO: test CRUD user
@Repository
public class UserRepositoryJpa extends AbstractPageableRepository<User, UserCondition> implements UserRepository {

    private final GroupRepository groupRepository;

    @Value("${users.page.size}")
    private Integer pageSize;

    @Autowired
    public UserRepositoryJpa(GroupRepository groupRepository) {
        entityClass = User.class;
        this.groupRepository = groupRepository;
    }

    @Override
    public PagedEntityList<User> getPageableList(UserCondition condition, Integer page, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<User> fromUser = criteriaQuery.from(User.class);
        criteriaQuery.distinct(true);
        criteriaQuery.select(criteriaBuilder.tuple(fromUser));

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqual(fromUser.get(User_.isActivated), condition.getIsActivated())
                .andEqual(fromUser.get(User_.isActive), condition.getIsActive())
                .andIn(fromUser.join(User_.groups, JoinType.INNER), condition.getGroups())
                .andNotEqual(fromUser.get(User_.id), condition.getShowAnonymous() == null || !condition.getShowAnonymous() ? ANONYMOUS_ID : null)
                .build();

        if(resultCondition != null) {
            criteriaQuery.where(resultCondition);
        }

        Order nickNameDesc = criteriaBuilder.asc(fromUser.get(User_.nickName));
        criteriaQuery.orderBy(nickNameDesc);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        PagedEntityList<User> users = makePagination(page, pageSize, criteriaBuilder, resultCondition, typedQuery);

        // Now retrieve user groups manually to avoid in-memory paging
        List<Group> groupsForUsers = groupRepository.getListByUsers(
                users.getList().stream().map(User::getId).collect(Collectors.toList()));

        Map<Integer, Set<Group>> groups = mapGroupsByUserId(groupsForUsers);

        for(User user : users.getList()) {
            user.setGroups(groups.get(user.getId()));
        }

        return users;
    }

    //TODO: test it
    @Override
    public List<User> getList(UserCondition condition, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> fromUser = criteriaQuery.from(User.class);
        criteriaQuery.select(fromUser);

        boolean joinWithOr = condition.getJoinWithOr();
        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .joinEqual(fromUser.get(User_.nickName), condition.getNickName(), joinWithOr, false)
                .joinEqual(fromUser.get(User_.email), condition.getEmail(), joinWithOr, false)
                .joinEqual(fromUser.get(User_.validationCode), condition.getValidationCode(), joinWithOr, false)
                .joinEqual(fromUser.get(User_.isActivated), condition.getIsActivated(), joinWithOr, false)
                .joinEqual(fromUser.get(User_.isActive), condition.getIsActive(), joinWithOr, false)
                .build();
        if(resultCondition != null) {
            criteriaQuery.where(resultCondition);
        }

        Order nickNameDesc = criteriaBuilder.asc(fromUser.get(User_.nickName));
        criteriaQuery.orderBy(nickNameDesc);

        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        return typedQuery.getResultList();
    }

    //TODO: test it
    @Override
    public User getUserByCondition(UserCondition condition, String entityGraphName) {
        List<User> result = getList(condition, entityGraphName);
        return result.size() == 0 ? null : result.get(0);
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.PROFILE, checkParents);
    }
}