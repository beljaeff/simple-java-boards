package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.model.PagedEntityList;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import com.github.beljaeff.sjb.repository.PageableRepository;
import com.github.beljaeff.sjb.repository.condition.Condition;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageableRepository<T extends IdentifiedActiveEntity, S extends Condition>
        extends AbstractBaseRepository<T, S> implements PageableRepository<T, S> {

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    protected List<T> getResultList(TypedQuery<Tuple> query) {
        List<Tuple> tuples = query.getResultList();
        List<T> ret = new ArrayList<>();
        for(Tuple tuple : tuples) {
            ret.add(tuple.get(0, entityClass));
        }
        return ret;
    }

    /**
     * If page not null - makes paginated query, otherwise retrieves all data
     */
    @Transactional(readOnly = true)
    protected PagedEntityList<T> makePagination(Integer page, Integer pSize,
                                      CriteriaBuilder builder, Predicate where, TypedQuery<Tuple> typedQuery) {
        Integer pageSize = pSize;
        PagedEntityList<T> ret = new PagedEntityList<>();
        if(page != null) {
            ret.setCurrentPage(page);

            long total = getTotal(builder, where, entityClass);
            ret.setTotal(total);

            if(pageSize == null || pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;

            typedQuery.setFirstResult((page-1)*pageSize);
            typedQuery.setMaxResults(pageSize);
            ret.setPageSize(pageSize);

            long totalPages = total % pageSize == 0 && total > 0 ?
                              total / pageSize
                             :total / pageSize + 1;

            ret.setTotalPages((int)totalPages);
        }

        List<T> resultList = getResultList(typedQuery);
        ret.setList(resultList);
        if(page == null) {
            ret.setTotal(resultList.size());
            ret.setPageSize(resultList.size());
            ret.setTotalPages(1);
            ret.setCurrentPage(1);
        }
        return ret;
    }

    @Transactional(readOnly = true)
    protected long getTotal(CriteriaBuilder builder, Predicate where, Class<? extends IdentifiedActiveEntity> entityClass) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(entityClass)));
        if(where != null) {
            countQuery.where(where);
        }
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Transactional(readOnly = true)
    public int getLastPage(CriteriaBuilder builder, Predicate where, Integer pSize, Class<? extends IdentifiedActiveEntity> entityClass) {
        Integer pageSize = pSize;
        long total = getTotal(builder, where, entityClass);
        if(pageSize == null || pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
        return        total % pageSize == 0 && total > 0 ?
                (int)(total / pageSize)
              : (int)(total / pageSize + 1);
    }
}
