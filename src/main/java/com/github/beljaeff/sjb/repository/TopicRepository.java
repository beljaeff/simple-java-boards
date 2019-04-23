package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.PostCondition;
import com.github.beljaeff.sjb.repository.condition.TopicCondition;
import com.github.beljaeff.sjb.model.Topic;

public interface TopicRepository extends PageableRepository<Topic, TopicCondition> {
    long getCountForUser(int userId);
    int getLastPage(PostCondition condition);
}