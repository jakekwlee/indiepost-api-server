package com.indiepost.repository

import com.indiepost.model.PostReading

interface PostReadingRepository {
    fun save(postReading: PostReading)

    fun save(postReading: PostReading, userId: Long, postId: Long): Long?

    fun findOne(id: Long?): PostReading?

    fun findOneByUserIdAndPostId(userId: Long, postId: Long): PostReading?

    fun findByUserIdAndPostIds(userId: Long, postIds: List<Long>): List<PostReading>

    fun setVisibility(userId: Long, visible: Boolean)

    fun deleteById(id: Long)
}
