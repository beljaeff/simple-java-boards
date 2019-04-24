package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.model.common.PositionedEntity;
import com.github.beljaeff.sjb.repository.PositionableRepository;
import org.springframework.transaction.annotation.Transactional;
import com.github.beljaeff.sjb.repository.condition.Condition;

abstract public class AbstractPositionableService<T extends PositionedEntity>
        extends AbstractHasAttachmentsService<T>
        implements PositionableService<T> {

    abstract protected PositionableRepository<T, ? extends Condition> getRepository();

    /**
     * @param id - category id to move
     * @param direction - true means move up, false means move down
     */
    @Transactional
    protected ActionStatusDto<T> changePosition(int id, boolean direction) {
        ActionStatusDto<T> actionStatus = getRepository().getForPositionChanging(id);
        T entity = actionStatus.getEntity();
        if(entity != null && actionStatus.getStatus()) {
            actionStatus.setStatus(false);
            T exchange = direction
                    ? getRepository().getNextPosition(entity)
                    : getRepository().getPrevPosition(entity);
            if(exchange != null) {
                int tmp = exchange.getPosition();
                exchange.setPosition(entity.getPosition());
                entity.setPosition(tmp);
                getRepository().update(entity);
                getRepository().update(exchange);
                actionStatus.setStatus(true);
            }
        }
        return actionStatus;
    }

    @Override
    public ActionStatusDto<T> up(int id) {
        return changePosition(id, true);
    }

    @Override
    public ActionStatusDto<T> down(int id) {
        return changePosition(id, false);
    }
}