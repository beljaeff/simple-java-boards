package com.github.beljaeff.sjb.service;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
import org.springframework.transaction.annotation.Transactional;

abstract public class AbstractHasAttachmentsService<T extends IdentifiedActiveEntity> extends AbstractCrudService<T> implements HasAttachments<T> {

    @Override
    @Transactional
    public ActionStatusDto<T> delete(int id) {
        removeAttachments(getWithGraph(id, null)); //TODO: implement service to remove lost attachments in case of exception here
        return super.delete(id);
    }
}