package com.indiepost.service

import com.indiepost.dto.post.*
import com.indiepost.enums.Types
import com.indiepost.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 17. 1. 14.
 */
interface AdminPostService {

    fun getAllTitles(): List<Title>

    fun findOne(id: Long): AdminPostResponseDto?

    fun createDraft(dto: AdminPostRequestDto): Long?

    fun createAutosave(requestDto: AdminPostRequestDto): Long?

    fun update(requestDto: AdminPostRequestDto)

    fun updateAutosave(requestDto: AdminPostRequestDto)

    fun deleteById(id: Long): Long?

    fun delete(post: Post): Long?

    fun find(status: Types.PostStatus, pageable: Pageable): Page<AdminPostSummaryDto>

    fun findIdsIn(ids: List<Long>, pageable: Pageable): Page<AdminPostSummaryDto>

    fun findText(text: String, status: Types.PostStatus, pageable: Pageable): Page<AdminPostSummaryDto>

    fun fullTextSearch(text: String, status: Types.PostStatus,
                       pageable: Pageable): Page<AdminPostSummaryDto>

    fun count(): Long

    fun count(query: PostQuery): Long

    fun findAllBylineNames(): List<String>

    fun bulkDeleteByStatus(status: Types.PostStatus)

    fun bulkDeleteByIds(ids: List<Long>)

    fun bulkStatusUpdate(ids: List<Long>, changeTo: Types.PostStatus)

}
