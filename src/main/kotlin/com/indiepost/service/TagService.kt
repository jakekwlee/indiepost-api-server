package com.indiepost.service

import com.indiepost.model.Tag

/**
 * Created by jake on 9/17/16.
 */
interface TagService {
    fun save(tag: Tag)

    fun findById(id: Long): Tag?

    fun findByName(name: String): Tag?

    fun findAll(): List<Tag>

    fun findAllToStringList(): List<String>

    fun findSelected(): List<String>

    fun updateSelected(tags: List<String>)

    fun findAll(page: Int, maxResults: Int): List<Tag>

    fun delete(tag: Tag)
}
