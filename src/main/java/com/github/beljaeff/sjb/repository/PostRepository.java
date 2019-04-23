package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.PostCondition;
import com.github.beljaeff.sjb.model.Post;

public interface PostRepository extends PageableRepository<Post, PostCondition> {}
