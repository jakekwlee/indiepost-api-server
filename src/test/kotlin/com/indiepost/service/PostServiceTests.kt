package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.TimelineRequest
import com.indiepost.helper.printToJson
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
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

/**
 * Created by jake on 17. 4. 23.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class PostServiceTests {
    @Inject
    private lateinit var postService: PostService

    @Test
    fun postsShouldHaveUniqueId() {
        val posts = postService.findPublicPosts(PageRequest.of(0, 10)).content
        var prevId: Long? = -1L
        for (post in posts) {
            assertThat(post.id).isNotNull()
            assertThat(post.id).isNotEqualTo(prevId)
            prevId = post.id
        }
        printToJson(posts)
    }

    @Test
    fun findPostsShouldReturnDtoListProperly() {
        val expected = 50
        val page = postService.findPublicPosts(PageRequest.of(0, expected))
        val posts = page.content
        assertThat(posts.size).isEqualTo(expected)
        assertThat(page.size).isEqualTo(expected)
        assertThat(posts[0].titleImageId).isNotNull()
        printToJson(page)
    }

    @Test
    fun fullTextSearchWorksAsExpected() {
        val text = "단편 영화"
        val page = postService.fullTextSearch(text, PageRequest.of(0, 5))
        val posts = page.content
        assertThat(posts).isNotNull.hasSize(5)
        for (dto in posts) {
            val highlight = dto.highlight
            val titleAndExcerpt = highlight?.title + highlight?.excerpt
            assertThat(titleAndExcerpt).contains(Arrays.asList("em", "단편", "영화"))
        }
        printToJson(posts)

    }

    @Test
    fun testFindByTagName() {
        val page = postService.findByTagName("일러스트", PageRequest.of(0, 100))
        val dtoList = page.content
        assertThat(dtoList.size).isGreaterThanOrEqualTo(9)
        assertThat(page.totalElements).isGreaterThanOrEqualTo(9)
        printToJson(dtoList)
    }

    @Test
    @WithMockUser("auth0|5a94f788fbf06a78e80360d0")
    fun findReadingHistory_shouldReturnResultProperly() {
        // TODO it takes too much time...
        val request = TimelineRequest()
        request.size = 100
        request.timepoint = Instant.now().toEpochMilli() / 1000
        val startTime = System.nanoTime()
        val result = postService.findReadingHistory(request)
        val endTime = System.nanoTime()
        assertThat(result.content.size).isEqualTo(result.numberOfElements)
        assertThat(result.content.size).isEqualTo(100)
        println("Running Time: " + (endTime - startTime) / 1000000 + "milliseconds")
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    fun findRelatedPostsById_shouldReturnRelatedPostsProperly() {
        val id = 7983L
        val size = 4
        val result = postService.findRelatedPostsById(id, PageRequest.of(0, 4))
        assertThat(result.content.size).isEqualTo(size)
        printToJson(result)
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    fun recommendations_shouldReturnRelatedPostsProperly() {
        val size = 16
        val result = postService.recommendations(PageRequest.of(0, size))
        assertThat(result.content.size).isEqualTo(size)
        printToJson(result)
    }
}
