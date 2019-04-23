package com.github.beljaeff.sjb.service.attachment;

import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.service.CrudService;

import java.util.List;

interface DbAttachmentService extends CrudService<Attachment> {
    Attachment get(int id);
    void deleteByIds(List<Integer> ids);
}