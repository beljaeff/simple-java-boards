package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.dto.form.conversation.TopicForm;
import com.github.beljaeff.sjb.model.Topic;
import com.github.beljaeff.sjb.service.CrudService;
import com.github.beljaeff.sjb.service.HasAttachments;
import com.github.beljaeff.sjb.dto.dto.conversation.TopicDto;

public interface TopicService extends CrudService<Topic>, HasAttachments<Topic>, CanCreateSaveForm<TopicForm> {
    FormPageDto<TopicDto, PostForm> getTopic(int id, int postPage, PostForm form);

    ActionStatusDto<Topic> approve(int id);

    ActionStatusDto<Topic> changeLock(int id);

    ActionStatusDto<Topic> changeSticky(int id);

    long getTopicsCountForUser(int userId);

    int getLastPage(int idTopic);

    FormPageDto<? extends BaseDto, TopicForm> getCreateForm(int idBoard, TopicForm formGiven);

    FormPageDto<? extends BaseDto, TopicForm> getEditForm(int idTopic, TopicForm formGiven);
}