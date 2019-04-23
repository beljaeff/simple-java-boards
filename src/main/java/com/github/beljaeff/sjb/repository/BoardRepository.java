package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.BoardCondition;
import com.github.beljaeff.sjb.model.Board;

public interface BoardRepository extends ListableRepository<Board, BoardCondition>,
        PositionableRepository<Board, BoardCondition> {}
