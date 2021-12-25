package com.github.beljaeff.sjb.repository.jpa;

import com.github.beljaeff.sjb.repository.condition.BoardCondition;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Board_;
import com.github.beljaeff.sjb.model.Category_;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.repository.BoardRepository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.github.beljaeff.sjb.model.EntityGraphNamesHelper.HINT_FETCH;

//TODO: position: add (?trigger), up/down
@Repository
public class BoardRepositoryJpa extends AbstractBaseRepository<Board, BoardCondition> implements BoardRepository {

    public BoardRepositoryJpa() {
        entityClass = Board.class;
    }

    /**
     * 'SELECT * FROM boards
     *           WHERE id_parent={parentId} AND id_category={categoryId}
     *           ORDER BY b.position ASC, b.title ASC'
     * @param condition - contains parentId and categoryId resulting boards belongs
     * @param entityGraphName - entity graph to apply
     */
    @Override
    public List<Board> getList(BoardCondition condition, String entityGraphName) {
        Assert.notNull(condition, "condition should be set");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Board> criteriaQuery = criteriaBuilder.createQuery(Board.class);
        Root<Board> fromBoard = criteriaQuery.from(Board.class);
        criteriaQuery.select(fromBoard);

        Predicate resultCondition = new ConditionBuilder(criteriaBuilder)
                .andEqualCheckingNull(fromBoard.get(Board_.parentBoard).get(Board_.id), condition.getParentId())
                .andEqualCheckingNull(fromBoard.get(Board_.category).get(Category_.id), condition.getCategoryId())
                .andEqual(fromBoard.get(Board_.isActive), condition.getIsActive())
                .build();
        if(resultCondition != null) {
            criteriaQuery.where(resultCondition);
        }

        Order positionBoardAsc = criteriaBuilder.asc(fromBoard.get(Board_.position));
        Order titleBoardAsc    = criteriaBuilder.asc(fromBoard.get(Board_.title));
        criteriaQuery.orderBy(positionBoardAsc, titleBoardAsc);

        TypedQuery<Board> typedQuery = entityManager.createQuery(criteriaQuery);

        if(entityGraphName != null) {
            EntityGraph<?> graph = entityManager.getEntityGraph(entityGraphName);
            typedQuery.setHint(HINT_FETCH, graph);
        }

        return typedQuery.getResultList();
    }

    /**
     * Get nearest board from board determined by given position. Search performs inside category/board of given board.
     * @param board given
     * @param direction determines searching direction.
     *                  If true then we searching for board with position < given,
     *                  false - we searching for board with position > given
     * @return board found or null
     */
    private Board getByPosition(Board board, boolean direction) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Board> criteriaQuery = criteriaBuilder.createQuery(Board.class);
        Root<Board> fromBoard = criteriaQuery.from(Board.class);
        criteriaQuery.select(fromBoard);

        Predicate filterCondition;
        if(board.getParentBoard() != null) {
            filterCondition = criteriaBuilder.equal(fromBoard.get(Board_.parentBoard).get(Board_.id), board.getParentBoard().getId());
        }
        else if(board.getCategory() != null) {
            filterCondition = criteriaBuilder.equal(fromBoard.get(Board_.category).get(Category_.id), board.getCategory().getId());
        }
        else {
            Predicate parentBoardIsNull = criteriaBuilder.isNull(fromBoard.get(Board_.parentBoard));
            Predicate categoryIsNull = criteriaBuilder.isNull(fromBoard.get(Board_.category));
            filterCondition = criteriaBuilder.and(parentBoardIsNull, categoryIsNull);
        }

        Predicate directionCondition;
        if(direction) {
            directionCondition = criteriaBuilder.lt(fromBoard.get(Board_.position), board.getPosition());
            criteriaQuery.orderBy(criteriaBuilder.desc(fromBoard.get(Board_.position)));
        }
        else {
            directionCondition = criteriaBuilder.gt(fromBoard.get(Board_.position), board.getPosition());
            criteriaQuery.orderBy(criteriaBuilder.asc(fromBoard.get(Board_.position)));
        }
        criteriaQuery.where(criteriaBuilder.and(filterCondition, directionCondition));

        TypedQuery<Board> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(1);

        List<Board> result = typedQuery.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Board getPrevPosition(Board board) {
        return getByPosition(board, false);
    }

    @Override
    public Board getNextPosition(Board board) {
        return getByPosition(board, true);
    }

    @Override
    public boolean isEntityActive(Integer entityId, Boolean checkParents) {
        return isEntityActive(entityId, EntityType.BOARD, checkParents);
    }

    @Override
    public List<CommonEntity> getBreadcrumbs(Integer entityId) {
        return getBreadcrumbs(entityId, EntityType.BOARD);
    }
}
