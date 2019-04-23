package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.AttachmentCondition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Attachment_;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.Post_;
import com.github.beljaeff.sjb.repository.AttachmentRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

//TODO: tests
@Repository
public class AttachmentRepositoryJpa extends AbstractBaseRepository<Attachment, AttachmentCondition> implements AttachmentRepository {

    public AttachmentRepositoryJpa() {
        entityClass = Attachment.class;
    }

    @Override
    public List<Attachment> getListByPosts(List<Integer> postIds) {
        if(isEmpty(postIds)) {
            return Collections.emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Attachment> criteriaQuery = criteriaBuilder.createQuery(Attachment.class);
        Root<Attachment> fromAttachment = criteriaQuery.from(Attachment.class);
        Join<Attachment, Post> leftJoinPost = fromAttachment.join(Attachment_.posts, JoinType.LEFT);
        criteriaQuery.distinct(true);
        criteriaQuery.select(fromAttachment);
        CriteriaBuilder.In<Integer> in = criteriaBuilder.in(leftJoinPost.get(Post_.id));
        postIds.forEach(in::value);
        criteriaQuery.where(in);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Attachment get(int id) {
        return entityManager.find(Attachment.class, id);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        Attachment attachment = entityManager.find(Attachment.class, id);
        if(attachment == null) {
            return false;
        }
        entityManager.remove(attachment);
        return entityManager.find(Attachment.class, id) == null;
    }

    @Transactional
    @Override
    public void deleteByIds(List<Integer> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Attachment> criteriaDelete = criteriaBuilder.createCriteriaDelete(Attachment.class);
        Root<Attachment> root = criteriaDelete.from(Attachment.class);
        criteriaDelete.where(root.get(Attachment_.id).in(ids));

        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.ATTACHMENT, checkParents);
    }
}
