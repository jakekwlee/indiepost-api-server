package com.indiepost.service

import com.indiepost.dto.CategoryDto
import com.indiepost.model.Category
import com.indiepost.repository.CategoryRepository
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 8/4/16.
 */
@Service
@Transactional
class CategoryServiceImpl @Inject
constructor(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun save(category: Category) {
        categoryRepository.persist(category)
    }

    override fun delete(category: Category) {
        categoryRepository.delete(category)
    }

    override fun getReference(id: Long): Category? {
        return categoryRepository.getReference(id)
    }

    override fun findById(id: Long): Category? {
        return categoryRepository.findById(id)
    }

    override fun findBySlug(slug: String): Category? {
        if (slug.isEmpty()) {
            return null
        }
        return categoryRepository.findBySlug(slug)
    }

    override fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    override fun findByParent(parent: Category): List<Category> {
        return categoryRepository.findByParentId(parent.id!!)
    }

    override fun getDtoList(): List<CategoryDto> {
        return this.findAll()
                .stream()
                .map { category ->
                    val categoryDto = CategoryDto()
                    BeanUtils.copyProperties(category, categoryDto)
                    categoryDto
                }
                .collect(Collectors.toList())
    }
}
