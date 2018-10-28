package com.indiepost.search

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.indiepost.IndiepostBackendApplication
import com.indiepost.service.PostService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 3. 16.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class TagSearchTest {

    @Inject
    private lateinit var postService: PostService

    @Test
    @Throws(JsonProcessingException::class)
    fun tagSearchShouldReturnPostsCorrectly() {
        val dtoList = postService.findByTagName("독립영화", PageRequest.of(0, 10))
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("\n\n*** Start serialize PostsByTagName ***\n\n")
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dtoList)
        println("Length of results: " + dtoList.content.size + " posts")
        println("Size of results: " + result.toByteArray().size / 1024.0 + " kb")
        println(result)
    }
}
