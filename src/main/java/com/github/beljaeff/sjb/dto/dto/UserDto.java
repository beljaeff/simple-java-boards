package com.github.beljaeff.sjb.dto.dto;

import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PageableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto implements PageableDto {
    private String nickName;
    private int goodKarma;
    private int badKarma;
    private int postsCount;
    private AttachmentDto avatar;
    private String groupColor;
    private String groupName;
    private boolean isBanned;
    private boolean isActivated;
    private String linkToProfile;
    private String location;
    private String signature;
}