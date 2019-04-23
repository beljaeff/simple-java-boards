package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.form.BaseForm;

public interface CanCreateSaveForm<T extends BaseForm> {
    void create(T form);
    void edit(T form);
}
