package com.indiepost.repository

import com.indiepost.dto.post.AdminPostSummaryDto
import com.indiepost.dto.post.Content
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.Title
import com.indiepost.enums.Types
import com.indiepost.model.Post
import com.indiepost.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 17. 1. 10.
 */
interface AdminPostRepository {

    fun getTitleList(): List<Title>

    fun persist(post: Post): Long?

    fun findOne(id: Long?): Post?

    fun delete(post: Post)

    fun deleteById(id: Long?)

    fun isExists(id: Long?): Boolean

    fun findByIdIn(ids: List<Long>): List<AdminPostSummaryDto>

    fun find(currentUser: User, pageable: Pageable): List<AdminPostSummaryDto>

    fun find(currentUser: User,
             status: Types.PostStatus,
             tag: String? = null,
             pageable: Pageable = PageRequest.of(0, 50)): List<AdminPostSummaryDto>

    fun findAll(): List<Post>

    fun findByIds(ids: List<Long>): List<Post>

    fun findText(text: String, currentUser: User, status: Types.PostStatus, pageable: Pageable): Page<AdminPostSummaryDto>

    fun findIds(currentUser: User, status: Types.PostStatus): List<Long>

    fun findAllDisplayNames(): List<String>

    fun count(): Long

    fun count(postQuery: PostQuery): Long

    fun count(status: Types.PostStatus, currentUser: User, tag: String? = null): Long

    fun findScheduledToBePublished(): List<Post>

    fun disableSplashPosts()

    fun disableFeaturedPosts()

    fun findContentListIncludingVideo(): List<Content>

    fun findBrokenPosts(pageable: Pageable): List<AdminPostSummaryDto>

    fun countBrokenPosts(): Long
}
