package com.github.beljaeff.sjb.repository.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCondition implements Condition {
    private Integer parentBoardId;
    private Boolean isActive;
}