package com.indiepost.repository

import com.indiepost.model.Category

/**
 * Created by jake on 7/26/16.
 */
interface CategoryRepository {

    fun persist(category: Category)

    fun delete(category: Category)

    fun getReference(id: Long): Category?

    fun findById(id: Long): Category?

    fun findBySlug(slug: String): Category?

    fun findByParentId(parentId: Long): List<Category>

    fun findAll(): List<Category>
}
