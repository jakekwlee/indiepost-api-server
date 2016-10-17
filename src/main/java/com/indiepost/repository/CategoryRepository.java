package com.indiepost.repository;

import com.indiepost.model.Category;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface CategoryRepository {

    void save(Category category);

    void update(Category category);

    void delete(Category category);

    Category findById(Long id);

    Category findBySlug(String slug);

    List<Category> findByParentId(Long parentId);

    List<Category> findAll();
}
