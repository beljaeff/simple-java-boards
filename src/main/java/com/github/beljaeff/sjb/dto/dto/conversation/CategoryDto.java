package com.github.beljaeff.sjb.dto.dto.conversation;

import lombok.Getter;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;

import java.util.List;

@Getter
@Setter
public class CategoryDto extends BaseDto {
    private String title;
    private String link;
    private List<BoardDto> boards;
}