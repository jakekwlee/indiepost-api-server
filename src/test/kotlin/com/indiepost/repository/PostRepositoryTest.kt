package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import com.indiepost.enums.Types
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
@Transactional
class PostRepositoryTest {

    @Inject
    private lateinit var repository: PostRepository

    @Test
    fun findByIdsShouldReturnPostsInRequestedOrder() {
        val postIds = Arrays.asList(5891L, 1679L, 5929L)
        val posts = repository.findByIds(postIds)
        val resultIds = posts.stream()
                .map<Long> { p -> p.id }
                .collect(Collectors.toList())
        assertThat(postIds).isEqualTo(resultIds)
    }

    @Test
    fun getStatusByIdShouldReturnPostStatusCorrectly() {
        val postId = 5929L
        val status = repository.getStatusById(postId)
        assertThat(status).isEqualTo(Types.PostStatus.PUBLISH)
    }
}
