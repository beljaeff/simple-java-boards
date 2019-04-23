package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

import java.util.List;

public interface CrudService<T extends IdentifiedActiveEntity> {
    T get(int id);

    T getWithGraph(int id, String entityGraphName);

    T save(T entity);

    ActionStatusDto<T> delete(int id);

    ActionStatusDto<T> changeActive(int id);

    List<BreadcrumbDto> getBreadcrumbs(Integer idEntity);

    List<BreadcrumbDto> getAddBreadcrumbs(Integer idEntity, String title);

    List<BreadcrumbDto> getEditBreadcrumbs(Integer idEntity, String title);
}