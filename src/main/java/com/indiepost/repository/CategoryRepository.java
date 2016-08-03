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

    Category findById(int id);

    Category findBySlug(String slug);

    List<Category> findByParent(Category parent);

    List<Category> findAll();
}
