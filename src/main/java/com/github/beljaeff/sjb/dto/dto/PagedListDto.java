package com.github.beljaeff.sjb.dto.dto;

import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.pagination.PageableDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedListDto extends BaseDto {
    private PaginatedDto<? extends PageableDto> list;
}
