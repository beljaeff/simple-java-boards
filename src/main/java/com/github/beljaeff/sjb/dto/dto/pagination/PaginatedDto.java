package com.github.beljaeff.sjb.dto.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedDto<T extends PageableDto> {
    private long total;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<T> list = new ArrayList<>();
}