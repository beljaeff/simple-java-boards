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
public class TopicCondition implements Condition {
    private int boardId;
    private Boolean isSticky;
    private Boolean isLocked;
    private Boolean isApproved;
    private Boolean isActive;

    private Boolean postsIsActive;
    private Boolean postsIsApproved;
}