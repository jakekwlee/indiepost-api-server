package com.indiepost.repository

import com.indiepost.dto.StaticPageDto
import com.indiepost.enums.Types
import com.indiepost.model.StaticPage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 17. 3. 5.
 */
interface StaticPageRepository {

    fun save(staticPage: StaticPage): Long?

    fun findById(id: Long?): StaticPage

    fun update(staticPage: StaticPage)

    fun delete(staticPage: StaticPage)

    fun find(pageable: Pageable): Page<StaticPageDto>

    fun find(pageable: Pageable, pageStatus: Types.PostStatus): Page<StaticPageDto>

    fun count(): Long

    fun count(pageStatus: Types.PostStatus): Long

    fun findBySlug(slug: String): StaticPage?

    fun bulkUpdateStatusByIds(ids: List<Long>, status: Types.PostStatus)

    fun bulkDeleteByIds(ids: List<Long>)

    fun bulkDeleteByStatus(status: Types.PostStatus)

    fun isExists(id: Long?): Boolean
}
