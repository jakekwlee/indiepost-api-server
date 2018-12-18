package com.indiepost.repository

import com.indiepost.model.Post
import com.indiepost.model.Profile

interface PostMigrationRepository {
    fun selectAllPostsWhereNotProfileSet(): List<Post>
    fun findProfileByEtc(text: String): Profile?
}