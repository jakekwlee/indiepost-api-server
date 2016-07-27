package com.indiepost.repository;

import com.indiepost.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category getCategoryBySlug(String slug);

    List<Category> getCategoriesByParent(Category parent);
}
