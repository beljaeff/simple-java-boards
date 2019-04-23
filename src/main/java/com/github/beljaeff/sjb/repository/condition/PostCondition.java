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
public class PostCondition implements Condition {
    private int topicId;
    private Boolean isSticky;
    private Boolean isApproved;
    private Boolean isActive;
}
