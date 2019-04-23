package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.repository.condition.CategoryCondition;
import com.github.beljaeff.sjb.model.Category;

public interface CategoryRepository extends ListableRepository<Category, CategoryCondition>,
                                            PositionableRepository<Category, CategoryCondition> {}
