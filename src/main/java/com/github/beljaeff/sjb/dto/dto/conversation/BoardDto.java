package com.github.beljaeff.sjb.dto.dto.conversation;

import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;

import java.util.List;

@Getter
@Setter
public class BoardDto extends BaseDto {
    private String title;
    private String link;
    private String description;
    private String icon;
    private List<BoardDto> boards;
    private int topicsCount;
    private int postsCount;
    private TopicDto lastTopic;
    private PaginatedDto<TopicDto> topics;
}
