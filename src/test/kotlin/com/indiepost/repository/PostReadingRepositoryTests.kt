package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import com.indiepost.model.PostReading
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
@Transactional
class PostReadingRepositoryTests {
    @Inject
    private lateinit var repository: PostReadingRepository

    private var insertedId: Long? = null

    @BeforeEach
    fun beforeTest() {
        val postReading = PostReading()
        postReading.created = LocalDateTime.now()
        postReading.lastRead = LocalDateTime.now()
        insertedId = repository.save(postReading, 500L, 500L)
    }

    @Test
    fun findOneByUserIdAndPostId_shouldReturnAnUserReadingProperly() {
        val postReading = repository.findOneByUserIdAndPostId(500L, 500L)
        assertThat(postReading).isNotNull
    }

    @Test
    fun findOne_shouldReturnAnUserReadingProperly() {
        val postReading = repository.findOne(insertedId)
        assertThat(postReading).isNotNull
    }
}
