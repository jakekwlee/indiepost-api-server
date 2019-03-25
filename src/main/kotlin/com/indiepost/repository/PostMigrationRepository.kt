package com.indiepost.repository

import com.indiepost.model.PTag
import com.indiepost.model.Post
import com.indiepost.model.Profile
import com.indiepost.model.Tag

interface PostMigrationRepository {
    fun selectAllPostsWhereNotProfileSet(): List<Post>
    fun findProfileByEtc(text: String): Profile?
    fun selectAllPostsWherePrimaryTagNotSet(): List<Post>
    fun selectPostById(id: Long): Post?
    fun saveTag(tag: Tag)
    fun addTagsToCategoriesIfNotExists()
    fun selectATagByName(text: String): Tag?
    fun isTagAttached(postId: Long, tagId: Long): Boolean
    fun findAllPrimaryTags(): List<PTag>
    fun isTagExists(text: String): Boolean
}