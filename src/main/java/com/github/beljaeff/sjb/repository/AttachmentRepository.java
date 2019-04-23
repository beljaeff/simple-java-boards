package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.AttachmentCondition;
import com.github.beljaeff.sjb.model.Attachment;

import java.util.List;

public interface AttachmentRepository extends BaseRepository<Attachment, AttachmentCondition> {
    Attachment get(int id);

    boolean delete(int id);

    void deleteByIds(List<Integer> ids);

    List<Attachment> getListByPosts(List<Integer> postIds);
}