package com.indiepost.repository.elasticsearch

import com.indiepost.enums.Types
import com.indiepost.model.User
import com.indiepost.model.Word
import com.indiepost.model.elasticsearch.PostEs
import org.springframework.data.domain.Pageable

interface PostEsRepository {

    fun testConnection(): Boolean

    fun indexExist(): Boolean

    fun createIndex(): Boolean

    fun deleteIndex(): Boolean

    fun buildIndex(posts: List<PostEs>)

    fun rebuildIndices(posts: List<PostEs>)

    fun search(text: String, status: Types.PostStatus, pageable: Pageable): List<PostEs>

    fun search(text: String, status: Types.PostStatus, currentUser: User, pageable: Pageable): List<PostEs>

    fun moreLikeThis(postIds: List<Long>, status: Types.PostStatus, pageable: Pageable): List<Long>

    fun recommendation(bookmarkedPostIds: List<Long>, historyPostIds: List<Long>, status: Types.PostStatus, pageable: Pageable): List<Long>

    fun count(text: String, status: Types.PostStatus): Int

    fun count(text: String, status: Types.PostStatus, currentUser: User?): Int

    fun findById(id: Long?): PostEs?

    fun index(post: PostEs)

    fun bulkIndex(posts: List<PostEs>)

    fun update(post: PostEs)

    fun deleteById(id: Long?)

    fun delete(postEs: PostEs)

    fun bulkDelete(ids: List<Long>)

    fun updateDictionary(words: List<Word>)
}
