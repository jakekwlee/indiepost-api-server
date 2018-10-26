package com.indiepost.service

import com.indiepost.dto.CategoryDto
import com.indiepost.model.Category

/**
 * Created by jake on 8/4/16.
 */
interface CategoryService {

    fun getDtoList(): List<CategoryDto>

    fun save(category: Category)

    fun delete(category: Category)

    fun getReference(id: Long?): Category?

    fun findById(id: Long?): Category?

    fun findBySlug(slug: String?): Category?

    fun findAll(): List<Category>

    fun findByParent(parent: Category): List<Category>
}