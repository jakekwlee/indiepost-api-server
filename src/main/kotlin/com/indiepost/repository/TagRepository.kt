package com.indiepost.repository

import com.indiepost.dto.SelectedTagDto
import com.indiepost.model.Tag
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 9/17/16.
 */
interface TagRepository {

    fun save(tag: Tag)

    fun findByTagName(name: String): Tag?

    fun findById(id: Long?): Tag?

    fun findAll(): List<Tag>

    fun findAll(pageable: Pageable): List<Tag>

    fun findByNameIn(tagNames: List<String>): List<Tag>

    fun findSelected(): List<SelectedTagDto>

    fun delete(tag: Tag)

    fun updateSelected(tagNames: List<String>)
}
