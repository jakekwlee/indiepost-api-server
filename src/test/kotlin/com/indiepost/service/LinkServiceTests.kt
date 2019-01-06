package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import com.indiepost.helper.printToJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class LinkServiceTests {
    @Inject
    private lateinit var linkService: LinkService

    @Test
    fun searchMovies_shouldWorkProperly() {
        val text = "스타워즈"
        val result = linkService.searchMovies(text, 10)
        assertThat(result).isNotEmpty()
        printToJson(result)
    }

    @Test
    fun searchBooks_shouldWorkProperly() {
        val text = "희지의 세계"
        val result = linkService.searchBooks(text, 1)
        assertThat(result).isNotEmpty()
        printToJson(result)
    }
}