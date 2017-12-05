package com.indiepost.service;

import com.indiepost.dto.CategoryDto;
import com.indiepost.model.Category;
import com.indiepost.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 8/4/16.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        categoryRepository.update(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public Category getReference(Long id) {
        return categoryRepository.getReference(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByParent(Category parent) {
        return categoryRepository.findByParentId(parent.getId());
    }

    @Override
    public List<CategoryDto> getDtoList() {
        return this.findAll()
                .stream()
                .map(category -> {
                    CategoryDto categoryDto = new CategoryDto();
                    BeanUtils.copyProperties(category, categoryDto);
                    return categoryDto;
                })
                .collect(Collectors.toList());
    }
}
