package com.indiepost.service;

import com.indiepost.dto.CategoryDto;
import com.indiepost.model.Category;

import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
public interface CategoryService {
    void save(Category category);

    void delete(Category category);

    Category getReference(Long id);

    Category findById(Long id);

    Category findBySlug(String slug);

    List<Category> findAll();

    List<Category> findByParent(Category parent);

    List<CategoryDto> getDtoList();
}