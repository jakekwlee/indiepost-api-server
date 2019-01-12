package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.post.PostQuery
import com.indiepost.enums.Types.PostStatus.PUBLISH
import com.indiepost.model.Post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(IndiepostBackendApplication::class))
@WebAppConfiguration
@Transactional
class AdminPostRepositoryTests {
    @Inject
    private lateinit var repository: AdminPostRepository

    @Test
    fun getTitleList_shouldReturnPostTitlesProperly() {
        val titleList = repository.getTitleList()
        val query = PostQuery.Builder(PUBLISH).build()
        assertThat(titleList).hasSize(repository.count(query).toInt())
    }

    @Test
    fun persist_shouldPersistPostContentWith_UTF8MB4_properly() {
        val testPost = repository.findOne(500L)
        val post = Post(
                title = "",
                content = "\uD83C\uDF40⭕️Swimming pool ⭕️\uD83C\uDF40",
                createdAt = LocalDateTime.now(),
                modifiedAt = LocalDateTime.now(),
                publishedAt = LocalDateTime.now(),
                displayName = "INDIEPOST",
                excerpt = "",
                status = PUBLISH
        )
        post.categoryId = 2L // Film
        post.primaryTagId = 8739L // Music
        post.author = testPost!!.author
        post.editor = testPost.editor
        repository.persist(post)
    }
}
