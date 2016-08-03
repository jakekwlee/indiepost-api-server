package com.indiepost.service;

import com.indiepost.model.Category;

import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
public interface CategoryService {
    void save(Category category);

    void update(Category category);

    void delete(Category category);

    Category findById(int id);

    Category findBySlug(String slug);

    List<Category> findAll();

    List<Category> findByParent(Category parent);
}
