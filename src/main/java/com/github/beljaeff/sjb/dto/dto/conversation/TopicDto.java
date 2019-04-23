package com.github.beljaeff.sjb.dto.dto.conversation;

import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PageableDto;
import com.github.beljaeff.sjb.dto.dto.UserDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;

@Getter
@Setter
public class TopicDto extends BaseDto implements PageableDto {
    private String title;
    private String link;
    private String dateCreate;
    private UserDto author;
    private long viewsCount;
    private long postsCount;
    private PostDto lastPost;
    private PaginatedDto<PostDto> posts;
    private boolean isApproved;
    private boolean isLocked;
    private boolean isSticky;
}