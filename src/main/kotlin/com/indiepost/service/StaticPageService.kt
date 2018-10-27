package com.indiepost.service

import com.indiepost.dto.StaticPageDto
import com.indiepost.enums.Types
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 17. 3. 5.
 */
interface StaticPageService {

    fun save(dto: StaticPageDto): Long

    fun update(dto: StaticPageDto)

    fun findById(id: Long): StaticPageDto?

    fun findBySlug(slug: String): StaticPageDto?

    fun find(pageable: Pageable): Page<StaticPageDto>

    fun find(status: Types.PostStatus, pageable: Pageable): Page<StaticPageDto>

    fun delete(staticPageDto: StaticPageDto)

    fun deleteById(id: Long)

    fun count(): Long

    fun bulkUpdateStatus(ids: List<Long>, status: Types.PostStatus)

    fun bulkDeleteByIds(ids: List<Long>)

    fun emptyTrash()
}
