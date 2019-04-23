package com.github.beljaeff.sjb.dto.form.conversation;

import com.github.beljaeff.sjb.validator.TopicFirstMessageCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@TopicFirstMessageCheck(message = "{topic.form.message.empty.or.too.big}", maxSize = 4096)
public class TopicForm extends AbstractAttachmentForm {
    @PositiveOrZero(message = "{topic.form.topic.incorrect}")
    private int id;

    @PositiveOrZero(message = "{topic.form.board.incorrect}")
    private Integer idBoard;

    @NotBlank(message = "{topic.form.title.empty}")
    @Size(min = 2, max = 128, message = "{topic.form.title.size.incorrect}")
    private String title;

    private String message;

    //TODO: implement dictionary and validator
    @NotBlank(message = "{topic.form.icon.empty}")
    private String icon = "far fa-circle";

    private Boolean isSticky;

    private Boolean isLocked;

    private Boolean isApproved;

    private Boolean isActive;
}