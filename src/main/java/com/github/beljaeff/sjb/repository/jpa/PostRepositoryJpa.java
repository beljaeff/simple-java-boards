package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.PostCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Post_;
import com.github.beljaeff.sjb.model.Topic_;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.repository.AttachmentRepository;
import com.github.beljaeff.sjb.repository.GroupRepository;
import com.github.beljaeff.sjb.repository.PostRepository;

import javax.persistence.EntityGraph;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.beljaeff.sjb.model.EntityGraphs.HINT_FETCH;
import static com.github.beljaeff.sjb.util.Utils.mapAttachmentsByPostId;
import static com.github.beljaeff.sjb.util.Utils.mapGroupsByUserId;

@Repository
public class PostRepositoryJpa extends AbstractPageableRepository<Post, PostCondition> implements PostRepository {

    @Value("${posts.page.size}")
    private Integer pageSize;

    private final GroupRepository groupRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public PostRepositoryJpa(GroupRepository groupRepository, AttachmentRepository attachmentRepository) {
        entityClass = Post.class;
        this.groupRepository = groupRepository;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * SELECT * FROM posts WHERE topic_id={topicId} ORDER BY is_sticky DESC, position ASC, date_create ASC
     * @param condition - post condition with posts topic id
     * @param page - page to display (1..n). If null - all elements selected
     * @param entityGraphName - entity graph to apply, null otherwise
     */
    //TODO: refactor tests
    @Transactional(readOnly = true)
    @Override
    public PagedEntityList<Post> getPageableList(PostCondition condition, Integer page, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Post> fromPost = criteriaQuery.from(Post.class);
        criteriaQuery.select(criteriaBuilder.tuple(fromPost));

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqual(fromPost.get(Post_.topic).get(Topic_.id), condition.getTopicId())
                .andEqual(fromPost.get(Post_.isApproved),           condition.getIsApproved())
                .andEqual(fromPost.get(Post_.isActive),             condition.getIsActive())
                .build();
        criteriaQuery.where(resultCondition);

        Order isStickyDesc  = criteriaBuilder.desc(fromPost.get(Post_.isSticky));
        Order dateCreateAsc = criteriaBuilder.asc (fromPost.get(Post_.dateCreate));
        criteriaQuery.orderBy(isStickyDesc, dateCreateAsc);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        PagedEntityList<Post> posts = makePagination(page, pageSize, criteriaBuilder, resultCondition, typedQuery);

        // Now retrieve user groups and attachments manually to avoid in-memory paging
        Map<Integer, List<Attachment>> attachments =
                mapAttachmentsByPostId(
                        attachmentRepository.getListByPosts(
                                posts.getList().stream().map(Post::getId).collect(Collectors.toList())));
        Map<Integer, Set<Group>> groups =
                mapGroupsByUserId(
                        groupRepository.getListByUsers(
                                posts.getList().stream().map(Post::getAuthor).map(User::getId).collect(Collectors.toList())));

        for(Post post : posts.getList()) {
            post.setAttachments(attachments.get(post.getId()));
            post.getAuthor().setGroups(groups.get(post.getAuthor().getId()));
        }

        return posts;
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.POST, checkParents);
    }

    @Override
    public List<CommonEntity> getBreadcrumbs(Integer entityId) {
        return getBreadcrumbs(entityId, EntityType.POST);
    }
}
