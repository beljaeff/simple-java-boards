package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;

public interface HasAttachments<T extends IdentifiedActiveEntity> {
    void removeAttachments(T entity);
}