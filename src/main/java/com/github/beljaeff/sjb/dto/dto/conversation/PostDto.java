package com.github.beljaeff.sjb.dto.dto.conversation;

import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.UserDto;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PageableDto;

import java.util.List;

@Getter
@Setter
public class PostDto extends BaseDto implements PageableDto {
    private String dateCreate;
    private UserDto author;
    private String dateLastUpdate;
    private int karma;
    private String link;
    private String body;
    private List<AttachmentDto> attachments;
    private boolean isApproved;
    private boolean isSticky;
}
