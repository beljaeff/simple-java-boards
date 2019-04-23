package com.github.beljaeff.sjb.dto.dto.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.BreadcrumbDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T extends BaseDto> {
    private T entity;
    private String title;
    private List<BreadcrumbDto> breadcrumbs;

    public PageDto(T entity, List<BreadcrumbDto> breadcrumbs) {
        this.entity = entity;
        this.breadcrumbs = breadcrumbs;
    }
}