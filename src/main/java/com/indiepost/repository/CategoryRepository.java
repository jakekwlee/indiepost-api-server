package com.indiepost.repository;

import com.indiepost.model.Category;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface CategoryRepository {

    Category getCategoryBySlug(String slug);

    List<Category> getCategoriesByParent(Category parent);
}
