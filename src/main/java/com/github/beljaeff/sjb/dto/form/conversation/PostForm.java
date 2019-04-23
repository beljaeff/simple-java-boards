package com.github.beljaeff.sjb.dto.form.conversation;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PostForm extends AbstractAttachmentForm {
    @PositiveOrZero(message = "{post.form.post.incorrect}")
    private int id;

    @NotBlank(message = "{post.form.message.empty}")
    @Size(max = 4096, message = "{post.form.message.too.big}")
    private String message;

    @PositiveOrZero(message = "{post.form.topic.incorrect}")
    private Integer idTopic;

    @PositiveOrZero(message = "{post.form.parent.post.incorrect}")
    private Integer idParent;

    private Boolean isSticky;

    private Boolean isApproved;

    private Boolean isActive;

    // Post page inside topic came from
    private Integer fromPage;

    // Post page inside topic to return
    private Integer toPage;
}