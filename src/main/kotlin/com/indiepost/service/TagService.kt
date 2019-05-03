package com.indiepost.service

import com.indiepost.dto.SelectedTagDto
import com.indiepost.dto.TagDto
import com.indiepost.enums.Types
import com.indiepost.model.Tag
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 9/17/16.
 */
interface TagService {
    fun save(tag: Tag)

    fun findById(id: Long): Tag?

    fun findByName(name: String): Tag?

    fun find(): List<Tag>

    fun findAllToStringList(): List<String>

    fun findSelected(): List<SelectedTagDto>

    fun updateSelected(tags: List<String>)

    fun delete(tag: Tag)

    fun find(pageable: Pageable, tagType: Types.TagType?, query: String?): List<TagDto>
}
