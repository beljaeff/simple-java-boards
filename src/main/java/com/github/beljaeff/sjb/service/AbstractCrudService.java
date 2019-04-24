package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.enums.ErrorCode;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.exception.PersistenceException;
import com.github.beljaeff.sjb.model.CommonEntity;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import com.github.beljaeff.sjb.repository.BaseRepository;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
abstract public class AbstractCrudService<T extends IdentifiedActiveEntity> implements CrudService<T> {

    protected RecordService recordService;

    @Autowired
    public void setRecordService(RecordService recordService) {
        this.recordService = recordService;
    }

    abstract protected BaseRepository<T, ? extends Condition> getRepository();

    private String getEntityName() {
        return getRepository().getEntityClass().getSimpleName().toLowerCase();
    }

    @Override
    public T getWithGraph(int id, String entityGraphName) {
        T entity = getRepository().get(id, entityGraphName);
        if(entity == null) {
            String entityName = getEntityName();
            EntityType entityType = EntityType.getByName(entityName);
            ErrorCode errorCode = ErrorCode.getNotFoundByType(entityType);
            throw new NotFoundException(Collections.singletonList(errorCode));
        }
        return entity;
    }

    @Override
    public T get(int id) {
        return getWithGraph(id, null);
    }

    @Override
    public T save(T entity) {
        Assert.notNull(entity, "entity should be set");
        try {
            if (entity.getId() == 0) {
                getRepository().add(entity);
                return entity;
            }
            return getRepository().update(entity);
        }
        catch (Exception e) {
            log.warn("Got error during saving entity {}", entity);
            log.warn(e.getMessage(), e);
            throw new PersistenceException(e.getCause(), Collections.singletonList(ErrorCode.PERSIST_ERROR));
        }
    }

    @Override
    @Transactional
    public ActionStatusDto<T> delete(int id) {
        T entity = getRepository().get(id);
        ActionStatusDto<T> actionStatus = new ActionStatusDto<>();
        actionStatus.setEntity(entity);
        actionStatus.setStatus(getRepository().delete(id));
        return actionStatus;
    }

    @Override
    @Transactional
    public ActionStatusDto<T> changeActive(int id) {
        T entity = getRepository().get(id);
        ActionStatusDto<T> actionStatus = new ActionStatusDto<>();
        actionStatus.setEntity(entity);
        if(entity != null) {
            entity.setIsActive(!entity.getIsActive());
            save(entity);
            actionStatus.setStatus(true);
        }
        return actionStatus;
    }

    @Override
    public List<BreadcrumbDto> getBreadcrumbs(Integer idEntity) {
        List<BreadcrumbDto> breadcrumbsDto = new ArrayList<>();
        breadcrumbsDto.add(new BreadcrumbDto(HttpUtils.getRootPath(), recordService.getText("home.text.header")));
        if(idEntity == null) {
            return breadcrumbsDto;
        }

        List<CommonEntity> breadcrumbs = getRepository().getBreadcrumbs(idEntity);
        if(!isEmpty(breadcrumbs)) {
            for(CommonEntity breadcrumb : breadcrumbs) {
                if(breadcrumb == null || breadcrumb.getIdEntity() == null || breadcrumb.getType() == EntityType.EMPTY) {
                    log.debug("Empty breadcrumb {} found processing breadcrumbs request by id {}", breadcrumb, idEntity);
                    continue;
                }
                breadcrumbsDto.add(new BreadcrumbDto(breadcrumb));
            }
        }
        return breadcrumbsDto;
    }

    @Override
    public List<BreadcrumbDto> getAddBreadcrumbs(Integer idEntity, String title) {
        List<BreadcrumbDto> breadcrumbsDto = getBreadcrumbs(idEntity);
        addFormLink(breadcrumbsDto, title);
        return breadcrumbsDto;
    }

    @Override
    public List<BreadcrumbDto> getEditBreadcrumbs(Integer idEntity, String title) {
        List<BreadcrumbDto> breadcrumbsDto = getBreadcrumbs(idEntity);
        addFormLink(breadcrumbsDto, title);
        return breadcrumbsDto;
    }

    private void addFormLink(List<BreadcrumbDto> breadcrumbsDto, String title) {
        if(!isEmpty(breadcrumbsDto)) {
            breadcrumbsDto.add(new BreadcrumbDto("", title));
        }
    }
}
