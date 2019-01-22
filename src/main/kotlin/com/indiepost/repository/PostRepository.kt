package com.indiepost.repository

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

    fun count(postQuery: PostQuery): Long

    fun getStatusById(postId: Long): Types.PostStatus?

    fun findByIds(ids: List<Long>): List<PostSummaryDto>

    fun findScheduledPosts(): List<PostSummaryDto>

    fun query(postQuery: PostQuery, pageable: Pageable): Page<PostSummaryDto>

    fun findByPrimaryTagName(slug: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByTagName(tagName: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByProfileSlug(slug: String, pageable: Pageable): Page<PostSummaryDto>

    fun findPublicPosts(pageable: Pageable, includeFeatured: Boolean = true): Page<PostSummaryDto>

    fun fallbackSearch(text: String, pageable: Pageable): Page<PostSummaryDto>

    fun findRelatedPostsById(id: Long): Page<PostSummaryDto>

    fun findReadingHistoryByUserId(userId: Long, pageable: Pageable): Page<PostSummaryDto>

    fun findBookmarksByUserId(userId: Long, pageable: Pageable): Page<PostSummaryDto>
}