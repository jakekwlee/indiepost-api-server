package com.indiepost.repository

import com.indiepost.dto.Timeline
import com.indiepost.dto.TimelineRequest
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.PostSummaryDto
import com.indiepost.enums.Types
import com.indiepost.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 7/26/16.
 */
interface PostRepository {

    fun findById(id: Long): Post?

    fun count(): Long

    fun count(query: PostQuery): Long

    fun getStatusById(postId: Long): Types.PostStatus?

    fun findByIds(ids: List<Long>): List<PostSummaryDto>

    fun findScheduledPosts(): List<PostSummaryDto>

    fun query(postQuery: PostQuery, pageable: Pageable): Page<PostSummaryDto>

    fun findByCategorySlug(slug: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByTagName(tagName: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByContributorFullName(fullName: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByStatus(status: Types.PostStatus, pageable: Pageable): Page<PostSummaryDto>

    fun fallbackSearch(text: String, pageable: Pageable): Page<PostSummaryDto>

    fun findRelatedPostsById(id: Long): Page<PostSummaryDto>

    fun findReadingHistoryByUserId(userId: Long, request: TimelineRequest): Timeline<PostSummaryDto>

    fun findBookmarksByUserId(userId: Long, request: TimelineRequest): Timeline<PostSummaryDto>
}