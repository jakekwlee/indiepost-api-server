package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.post.AdminPostRequestDto
import com.indiepost.dto.post.Title
import com.indiepost.enums.Types
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.Instant
import java.util.*
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class AdminPostServiceTests {

    @Inject
    private lateinit var service: AdminPostService

    private var insertedId: Long? = null

    @BeforeEach
    @WithMockUser(username = "auth0|5a94f76a5c798c2296fd51ae")
    fun insert() {
        val post = AdminPostRequestDto()
        post.tags = Arrays.asList("여행기", "콩자반", "일본영화", "쿠바", "아기다리고기다", "로망포르노", "로맨스")
        post.content = "test content"
        post.title = "test title"
        post.excerpt = "test except"
        post.categoryId = 2L
        post.displayName = "TEST name"
        post.publishedAt = Instant.now()
        insertedId = service.createDraft(post)
    }

    @Test
    @WithMockUser(username = "auth0|5a94f76a5c798c2296fd51ae")
    fun saveAutosave_shouldReturnCreatedPostId() {
        val post = AdminPostRequestDto()
        post.content = "test content"
        post.title = "test title"
        post.excerpt = "test except"
        post.categoryId = 2L
        post.displayName = "TEST name"
        post.publishedAt = Instant.now()
        val id = service.createAutosave(post)
        if (id != null) {
            insertedId = id
        }
        assertThat(id).isNotNull()
    }

    @Test
    @WithMockUser(username = "auth0|5a94f76a5c798c2296fd51ae")
    fun retrievedAdminPostResponseDto_shouldContainTagsWithProperOrder() {
        val id = insertedId ?: throw Exception()
        val dto = service.findOne(id)
        assertThat(dto!!.tags).isEqualTo(Arrays.asList("여행기", "콩자반", "일본영화", "쿠바", "아기다리고기다", "로망포르노", "로맨스"))
    }

    @Test
    @WithMockUser(username = "auth0|5a94f76a5c798c2296fd51ae")
    fun findText_shouldReturnResultProperly() {
        val page = service.findText("인스타그램", Types.PostStatus.PUBLISH, PageRequest.of(0, 500))
        assertThat(page.content).hasSize(18)

    }

    @Test
    @WithMockUser("auth0|5a94f76a5c798c2296fd51ae")
    fun getAllTitles_shouldWorkProperly() {
        val titles = service.getAllTitles()
        assertThat(titles).isNotNull.hasAtLeastOneElementOfType(Title::class.java)
    }
}
