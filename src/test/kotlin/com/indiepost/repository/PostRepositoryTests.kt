package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import com.indiepost.dto.post.PostSummaryDto
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
@Transactional
class PostRepositoryTests {

    @Inject
    private lateinit var postRepository: PostRepository

    @Test
    fun findByTagName_shouldReturnPostSummaryDtoListProperly() {
        val tagName = "디즈니"
        val pageSize = 5
        val page = postRepository.findByTagName(tagName, PageRequest.of(0, pageSize))
        val postList = page.content
        assertThat(postList).isNotNull
        assertThat(postList).hasAtLeastOneElementOfType(PostSummaryDto::class.java)
        assertThat(postList.size).isGreaterThanOrEqualTo(pageSize)
        assertThat(page.size).isGreaterThanOrEqualTo(pageSize)
    }

    @Test
    fun findByTagName_resultShouldContainAllElementProperly() {
        val tagName = "디즈니"
        val page = postRepository.findByTagName(tagName, PageRequest.of(0, 1))
        val postList = page.content
        assertThat(postList).isNotNull
        val post = postList[0]
        assertThat(post).isNotNull()
        assertThat(page.size).isGreaterThanOrEqualTo(1)
    }

    @Test
    fun fallbackSearch_returnPostListProperly() {
        val page = postRepository.fallbackSearch("황샤오량", PageRequest.of(0, 30))
        assertThat(page.totalElements).isGreaterThanOrEqualTo(1)
    }
}
