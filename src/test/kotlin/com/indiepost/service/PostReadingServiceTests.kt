package com.indiepost.service

import com.indiepost.NewIndiepostApplication
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
class PostReadingServiceTests {

    @Inject
    private lateinit var service: PostUserInteractionService

    @Test
    fun add_shouldAddUserReadingProperlyAndReturnId() {
        service.add(4L, 500L).let {
            assertThat(it).isNotNull()
        }
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    fun setInvisibleByPostId_shouldAddUserReadingProperly() {
        service.setInvisible(8051L)
        val postReading = service.findOne(1L) ?: throw Exception()
        assertThat(postReading.isVisible).isFalse()
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    fun setInvisibleAllByUserId_shouldAddUserReadingProperly() {
        service.setInvisibleAll()
        val pr = service.findOne(1) ?: throw Exception()
        assertThat(pr.isVisible).isFalse()
        service.setVisibleAll()
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    fun setInvisibleAllByUserId_shouldAddUserReadingProperlyAndReturnId() {
        service.setInvisibleAll()
        val pr = service.findOne(1) ?: throw Exception()
        assertThat(pr.isVisible).isFalse()
    }
}
