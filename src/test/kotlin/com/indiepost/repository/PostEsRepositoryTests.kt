package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import com.indiepost.enums.Types
import com.indiepost.helper.printToJson
import com.indiepost.repository.elasticsearch.PostEsRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(IndiepostBackendApplication::class))
@WebAppConfiguration
@Transactional
class PostEsRepositoryTests {

    @Inject
    private lateinit var postEsRepository: PostEsRepository

    @Inject
    private lateinit var userRepository: UserRepository

    @Test
    fun connectionShouldEstablishProperly() {
        val isConnected = postEsRepository.testConnection()
        assertThat(isConnected).isTrue()
    }

    @Test
    fun findByIdShouldReturnPostEsCorrectly() {
        val postId = 6740L
        val post = postEsRepository.findById(postId)
        assertThat(post).isNotNull()
        assertThat(post!!.id).isEqualTo(postId)
        assertThat(post.title).isNotEmpty()
        assertThat(post.excerpt).isNotEmpty()
        assertThat(post.bylineName).isNotEmpty()
        assertThat(post.content).isNotEmpty()
        assertThat(post.getTags().size).isGreaterThan(0)
        printToJson(post)
    }


    @Test
    fun searchTestWorksCorrectly() {
        val text = "무지갯빛 영화"
        val postEsList = postEsRepository.search(
                text,
                Types.PostStatus.PUBLISH,
                PageRequest.of(0, 24)
        )
        assertThat(postEsList).isNotNull.hasSize(3)
        val post = postEsList[0]
        assertThat(post.id)
                .isEqualTo(287L)
        assertThat(post.content).isNull()
        assertThat(post.status).isNull()
        assertThat(post.getTags()).hasSize(0)
        assertThat(post.getContributors()).hasSize(0)
        assertThat(post.title)
                .isNotEmpty()
                .contains("em", "무지갯", "영화")
        assertThat(post.excerpt)
                .isNotEmpty()
                .contains("em", "영화")
        printToJson(postEsList)
    }

    //   TODO @Test
    fun searchForAdminUserWorksCorrectly() {
        val text = "너의 췌장을"
        val userId = 12L
        val currentUser = userRepository.findById(userId) ?: return
        val postEsList = postEsRepository.search(
                text,
                Types.PostStatus.PUBLISH,
                currentUser,
                PageRequest.of(0, 20)
        )
        printToJson(postEsList)
        assertThat(postEsList).hasSize(2)
        for (post in postEsList) {
            assertThat(post.id).isNotNull()
            assertThat(post.content).isNull()
            assertThat(post.status).isNull()
            assertThat(post.getTags()).hasSize(0)
            assertThat(post.excerpt).isNull()
        }
    }

    @Test
    fun countShouldReturnValueExactly() {
        val status = Types.PostStatus.PUBLISH
        val text = "인디 음악"
        val count = postEsRepository.count(text, status)
        val posts = postEsRepository.search(text, status, PageRequest.of(0, 10000))
        assertThat(count).isNotZero().isEqualTo(posts.size)
        printToJson(count)
    }

    @Test
    fun moreLikeThis_shouldReturnRelatedPostList() {
        val postId = 8163L
        val size = 4
        val ids = postEsRepository.moreLikeThis(Arrays.asList(postId), Types.PostStatus.PUBLISH, PageRequest.of(0, size))
        assertThat(ids).hasSize(size)
    }

    @Test
    // TODO
    fun recommendation_shouldReturnRelatedPostList() {
        val bookmarked = Arrays.asList(129L, 8422L)
        val history = Arrays.asList(8543L, 5022L, 8480L)
        val size = 4
        val ids = postEsRepository.recommendation(bookmarked, history, Types.PostStatus.PUBLISH, PageRequest.of(0, size))
        assertThat(ids).hasSize(size)
    }
}
