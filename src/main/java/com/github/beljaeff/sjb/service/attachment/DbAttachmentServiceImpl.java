package com.github.beljaeff.sjb.service.attachment;

import com.github.beljaeff.sjb.repository.AttachmentRepository;
import com.github.beljaeff.sjb.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.repository.condition.Condition;
import com.github.beljaeff.sjb.service.AbstractCrudService;

import java.util.List;

@Service
class DbAttachmentServiceImpl extends AbstractCrudService<Attachment> implements DbAttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public DbAttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    protected BaseRepository<Attachment, ? extends Condition> getRepository() {
        return attachmentRepository;
    }

    @Override
    public Attachment get(int id) {
        return attachmentRepository.get(id);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        attachmentRepository.deleteByIds(ids);
    }
}