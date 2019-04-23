package com.github.beljaeff.sjb.service.conversation;

import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.dto.dto.page.PageDto;
import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.dto.dto.conversation.BoardDto;
import com.github.beljaeff.sjb.service.PositionableService;

public interface BoardService extends PositionableService<Board>, CanCreateSaveForm<BoardForm> {
    PageDto<BoardDto> getBoard(int id, int topicPage);
    FormPageDto<? extends BaseDto, BoardForm> getAddForm(BoardForm formGiven);
    FormPageDto<? extends BaseDto, BoardForm> getEditForm(int boardId, BoardForm formGiven);
}