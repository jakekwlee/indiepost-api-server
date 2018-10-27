package com.indiepost.service

import com.indiepost.dto.post.PostUserInteraction
import com.indiepost.model.Bookmark
import com.indiepost.model.PostReading

interface PostUserInteractionService {

    fun add(userId: Long, postId: Long): Long

    fun findOne(id: Long): PostReading?

    fun findUsersByPostId(postId: Long): PostUserInteraction?

    fun findOneByPostId(postId: Long): PostReading?

    fun findBookmark(userId: Long, postId: Long): Bookmark?

    fun setInvisible(postId: Long)

    fun setInvisibleAll()

    fun setVisibleAll()

    fun addBookmark(postId: Long)

    fun removeBookmark(postId: Long)

    fun removeAllUsersBookmarks()

    fun deleteById(id: Long)
}
