package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.form.conversation.PostForm;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.service.CrudService;
import com.github.beljaeff.sjb.service.HasAttachments;

public interface PostService extends CrudService<Post>, HasAttachments<Post>, CanCreateSaveForm<PostForm> {
    ActionStatusDto<Post> approve(int id);
    ActionStatusDto<Post> changeSticky(int id);
    FormPageDto<? extends BaseDto, PostForm> getCreateForm(int idReplyPost, PostForm formGiven);
    FormPageDto<? extends BaseDto, PostForm> getEditForm(int idPost, PostForm formGiven);
    ActionStatusDto<Post> removeAttachment(int id, int attachmentId);
}