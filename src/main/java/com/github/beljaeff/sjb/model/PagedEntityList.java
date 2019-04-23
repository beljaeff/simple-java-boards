package com.github.beljaeff.sjb.model;

import com.github.beljaeff.sjb.model.common.IdentifiedActiveEntity;
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
public class PagedEntityList<T extends IdentifiedActiveEntity> {
    private long total;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<T> list = new ArrayList<>();
}
