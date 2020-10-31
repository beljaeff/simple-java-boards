package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.CategoryCondition;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Board_;
import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.model.Category_;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.repository.CategoryRepository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.github.beljaeff.sjb.model.EntityGraphNamesHelper.HINT_FETCH;

@Repository
public class CategoryRepositoryJpa extends AbstractBaseRepository<Category, CategoryCondition> implements CategoryRepository {

    public CategoryRepositoryJpa() {
        entityClass = Category.class;
    }

    @Override
    public List<Category> getList(CategoryCondition condition, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
        Root<Category> fromCategory = criteriaQuery.from(Category.class);
        Join<Category, Board> leftJoinBoard = fromCategory.join(Category_.boards, JoinType.LEFT);
        criteriaQuery.distinct(true);
        criteriaQuery.select(fromCategory);

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqualCheckingNull(leftJoinBoard.get(Board_.parentBoard).get(Board_.id), condition.getParentBoardId())
                .andEqual(fromCategory.get(Category_.isActive), condition.getIsActive())
                .build();
        if(resultCondition != null) {
            criteriaQuery.where(resultCondition);
        }

        Order positionCategoryAsc = criteriaBuilder.asc(fromCategory.get(Category_.position));
        Order titleCategoryAsc    = criteriaBuilder.asc(fromCategory.get(Category_.title));
        criteriaQuery.orderBy(positionCategoryAsc, titleCategoryAsc);

        TypedQuery<Category> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        return typedQuery.getResultList();
    }

    /**
     * Get nearest category from category determined by given position of given category.
     * @param category given
     * @param direction determines searching direction.
     *                  If true then we searching for category with position < given,
     *                  false - we searching for category with position > given
     * @return category found or null
     */
    private Category getByPosition(Category category, boolean direction) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
        Root<Category> fromCategory = criteriaQuery.from(Category.class);
        criteriaQuery.select(fromCategory);

        if(direction) {
            criteriaQuery.where(criteriaBuilder.lt(fromCategory.get(Category_.position), category.getPosition()));
            criteriaQuery.orderBy(criteriaBuilder.desc(fromCategory.get(Category_.position)));
        }
        else {
            criteriaQuery.where(criteriaBuilder.gt(fromCategory.get(Category_.position), category.getPosition()));
            criteriaQuery.orderBy(criteriaBuilder.asc(fromCategory.get(Category_.position)));
        }

        TypedQuery<Category> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(1);

        List<Category> result = typedQuery.getResultList();
        return result.isEmpty()? null : result.get(0);
    }

    @Override
    public Category getPrevPosition(Category category) {
        return getByPosition(category, false);
    }

    @Override
    public Category getNextPosition(Category category) {
        return getByPosition(category, true);
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.CATEGORY, checkParents);
    }

    @Override
    public List<CommonEntity> getBreadcrumbs(Integer entityId) {
        return getBreadcrumbs(entityId, EntityType.CATEGORY);
    }
}