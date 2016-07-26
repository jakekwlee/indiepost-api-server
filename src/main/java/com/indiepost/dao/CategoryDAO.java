package com.indiepost.dao;

import com.indiepost.model.Category;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface CategoryDAO {

    Category getCagetoryById(int id);

    Category getCategoryBySlug(String slug);

    List<Category> getCategories();

    List<Category> getCategoriesByParent(Category parent);

    void add(Category category);

    void update(Category category);

    void delete(Category category);

    int count();
}
