package com.indiepost.service

import com.indiepost.dto.Timeline
import com.indiepost.dto.TimelineRequest
import com.indiepost.dto.post.PostDto
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.PostSummaryDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import java.time.LocalDateTime

/**
 * Created by jake on 7/30/16.
 */
interface PostService {

    fun findOne(id: Long): PostDto

    fun count(): Long

    fun count(postQuery: PostQuery): Long

    fun find(pageable: Pageable): Page<PostSummaryDto>

    fun findByCategorySlug(slug: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByTagName(tagName: String, pageable: Pageable): Page<PostSummaryDto>

    fun findByContributorFullName(fullName: String, pageable: Pageable): Page<PostSummaryDto>

    fun query(postQuery: PostQuery, pageable: Pageable): Page<PostSummaryDto>

    fun fullTextSearch(text: String, pageable: Pageable): Page<PostSummaryDto>

    fun moreLikeThis(id: Long?, pageable: Pageable): Page<PostSummaryDto>

    fun findRelatedPostsById(id: Long?, pageable: Pageable): Page<PostSummaryDto>

    fun recommendations(pageable: Pageable): Page<PostSummaryDto>

    fun findReadingHistory(request: TimelineRequest): Timeline<PostSummaryDto>

    fun findBookmarks(request: TimelineRequest): Timeline<PostSummaryDto>

    fun findByIds(ids: List<Long>): List<PostSummaryDto>

    fun findTopRatedPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<PostSummaryDto>

    fun findScheduledPosts(): List<PostSummaryDto>

    fun findPickedPosts(): List<PostSummaryDto>

    fun findSplashPost(): PostSummaryDto?

    fun findFeaturePost(): PostSummaryDto?
}