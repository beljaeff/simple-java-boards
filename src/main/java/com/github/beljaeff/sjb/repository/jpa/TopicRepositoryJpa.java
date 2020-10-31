package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Attachment_;
import com.github.beljaeff.sjb.model.Board_;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.EntityGraphNamesHelper;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Post_;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.model.Topic_;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.model.User_;
import com.github.beljaeff.sjb.repository.TopicRepository;
import com.github.beljaeff.sjb.repository.condition.PostCondition;
import com.github.beljaeff.sjb.repository.condition.TopicCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityGraph;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TopicRepositoryJpa extends AbstractPageableRepository<Topic, TopicCondition> implements TopicRepository {

    @Value("${topics.page.size}")
    private Integer pageSize;

    public TopicRepositoryJpa() {
        entityClass = Topic.class;
    }

    /**
     * @param condition - condition contains board id for topics
     * @param page - page to display (1..n). If null - all elements selected
     * @param entityGraphName - entity graph to apply, null otherwise
     */
    @Override
    public PagedEntityList<Topic> getPageableList(TopicCondition condition, Integer page, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Topic> fromTopic = criteriaQuery.from(Topic.class);

        Join<Topic, Post> postsCountJoin = fromTopic.join(Topic_.posts, JoinType.LEFT);
        Predicate resultPostsCountJoinCondition = new ConditionBuilder(criteriaBuilder)
                .andEqual(postsCountJoin.get(Post_.isActive), condition.getPostsIsActive())
                .andEqual(postsCountJoin.get(Post_.isApproved), condition.getPostsIsApproved())
                .build();
        if(resultPostsCountJoinCondition != null) {
            postsCountJoin.on(resultPostsCountJoinCondition);
        }

        // Hibernate don't set primary keys to group by (it set foreign keys instead of it)
        // in expressions like fromTopic.get(Topic_.lastPost).get(Post_.id)
        // so joins required to make group by properly
        Join<Topic, User> authorJoin = fromTopic.join(Topic_.author, JoinType.LEFT);
        Join<User, Attachment> authorAvatarJoin = authorJoin.join(User_.avatar, JoinType.LEFT);
        Join<Topic, Post> lastPostJoin = fromTopic.join(Topic_.lastPost, JoinType.LEFT);
        Join<Post, User> lpAuthorJoin = lastPostJoin.join(Post_.author, JoinType.LEFT);
        Join<User, Attachment> lpAuthorAvatarFetch = lpAuthorJoin.join(User_.avatar, JoinType.LEFT);

        criteriaQuery.multiselect(
            fromTopic,
            criteriaBuilder.count(postsCountJoin.get(Post_.id))
        );

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqual(fromTopic.get(Topic_.board).get(Board_.id), condition.getBoardId())
                .andEqual(fromTopic.get(Topic_.isApproved),           condition.getIsApproved())
                .andEqual(fromTopic.get(Topic_.isActive),             condition.getIsActive())
                .build();
        criteriaQuery.where(resultCondition);
        criteriaQuery.groupBy(fromTopic.get(Topic_.id),
                              lastPostJoin.get(Post_.id),
                              lpAuthorJoin.get(User_.id),
                              lpAuthorAvatarFetch.get(Attachment_.id),
                              authorJoin.get(User_.id),
                              authorAvatarJoin.get(Attachment_.id));

        Order isStickyDesc   = criteriaBuilder.desc(fromTopic.get(Topic_.isSticky));
        Order dateUpdateDesc = criteriaBuilder.desc(lastPostJoin.get(Post_.dateCreate));
        Order titleAsc       = criteriaBuilder.asc (fromTopic.get(Topic_.title));
        criteriaQuery.orderBy(isStickyDesc, dateUpdateDesc, titleAsc);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(EntityGraphNamesHelper.HINT_FETCH, graph);
        }

        return makePagination(page, pageSize, criteriaBuilder, resultCondition, typedQuery);
    }

    @Override
    protected List<Topic> getResultList(TypedQuery<Tuple> query) {
        List<Tuple> tuples = query.getResultList();
        List<Topic> ret = new ArrayList<>();
        for(Tuple tuple : tuples) {
            Topic topic = (Topic)tuple.get(0);
            topic.setPostsCount((long)tuple.get(1));
            ret.add(topic);
        }
        return ret;
    }

    @Override
    public long getCountForUser(int userId) {
        // Create query only to take condition. This query will never executed.
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Topic> query = criteriaBuilder.createQuery(Topic.class);
        Root<Topic> from = query.from(Topic.class);
        query.select(from);

        Predicate where = new ConditionBuilder(criteriaBuilder)
                .andEqual(from.get(Topic_.author).get(User_.id), userId)
                .build();

        query.where(where);
        entityManager.createQuery(query);

        return getTotal(criteriaBuilder, where, Topic.class);
    }

    @Override
    public int getLastPage(PostCondition condition) {
        // Create query only to take condition. This query will never executed.
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = criteriaBuilder.createQuery(Post.class);
        Root<Post> from = query.from(Post.class);

        Predicate where = new ConditionBuilder(criteriaBuilder)
                .andEqual(from.get(Post_.topic).get(Topic_.id), condition.getTopicId())
                .andEqual(from.get(Post_.isApproved),           condition.getIsApproved())
                .andEqual(from.get(Post_.isActive),             condition.getIsActive())
                .build();

        query.select(from).where(where);
        entityManager.createQuery(query);

        return getLastPage(criteriaBuilder, where, pageSize, Post.class);
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.TOPIC, checkParents);
    }

    @Override
    public List<CommonEntity> getBreadcrumbs(Integer entityId) {
        return getBreadcrumbs(entityId, EntityType.TOPIC);
    }
}