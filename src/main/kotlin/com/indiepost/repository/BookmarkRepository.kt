package com.indiepost.repository

import com.indiepost.model.Bookmark

interface BookmarkRepository {
    fun save(bookmark: Bookmark)

    fun create(userId: Long, postId: Long)

    fun delete(bookmark: Bookmark)

    fun findOneByUserIdAndPostId(userId: Long, postId: Long): Bookmark?

    fun findByUserIdAndPostIds(userId: Long, postIds: List<Long>): List<Bookmark>

    fun removeAllBookmarksByUserId(userId: Long)
}
