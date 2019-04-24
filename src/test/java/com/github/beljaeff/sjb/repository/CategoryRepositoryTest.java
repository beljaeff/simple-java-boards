package com.github.beljaeff.sjb.repository;

import com.github.beljaeff.sjb.model.Category;
import com.github.beljaeff.sjb.repository.condition.CategoryCondition;
import com.github.beljaeff.sjb.repository.jpa.CategoryRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: position tests
//TODO: test when delete category related boards will have null category
class CategoryRepositoryTest extends AbstractRepositoryTest<Category, CategoryCondition> {

    @Autowired
    private CategoryRepository repository;

    @Override
    public Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    public Category getEntity() {
        return new Category();
    }

    @Override
    protected CategoryCondition getCondition() {
        CategoryCondition condition = new CategoryCondition();
        condition.setParentBoardId(1);
        return condition;
    }

    @Override
    protected ListableRepository<Category, CategoryCondition> getRepository() {
        return repository;
    }

    @Override
    public ListableRepository<Category, CategoryCondition> getMockRepository() {
        return new CategoryRepositoryJpa();
    }

    @Test
    public void testGetAll() {
        CategoryCondition condition = new CategoryCondition();
        condition.setParentBoardId(1);
        verifyLists(condition, 3);
    }

    @Test
    public void testGetAllNullIdWithSort() {
        verifyLists(new CategoryCondition(), 3, 4, 2, 1);
    }

    @Test
    public void testGetAllIdNotExists() {
        CategoryCondition condition = new CategoryCondition();
        condition.setParentBoardId(1000);
        List<Category> list = getRepository().getList(condition, null);
        assertEquals(0, list.size());
    }

    @Transactional
    @Test
    public void testDeleteCategory() {
        int id = 4;
        assertTrue(repository.delete(id));
        entityManager.flush();
        entityManager.clear();
        // Verify category deleted
        assertNull(repository.get(id));
    }

    @Transactional
    @Test
    public void testAdd() {
        Category category = new Category();
        category.setTitle("new3");
        category.setIsActive(true);

        repository.add(category);

        List<Category> categories = repository.getList(new CategoryCondition(), null);
        assertNotNull(categories);
        assertEquals(5, categories.size());
    }

    @Test
    public void testUpdate() {
        int id = 4;
        Category category = repository.get(id);
        category.setTitle("ttl");
        repository.update(category);
        assertEquals(category.getTitle(), repository.get(id).getTitle());
    }
}
