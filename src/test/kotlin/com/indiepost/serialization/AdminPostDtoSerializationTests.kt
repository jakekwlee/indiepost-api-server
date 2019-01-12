package com.indiepost.serialization

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.post.PostFilter
import com.indiepost.enums.Types
import com.indiepost.service.AdminPostService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 22.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class AdminPostDtoSerializationTests {

    @Inject
    private lateinit var adminPostService: AdminPostService

    /**
     * Usage: CMS Datatable
     *
     * @throws JsonProcessingException
     */
    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    fun adminPostSummaryDtoListShouldSerializeCorrectly() {
        val page = adminPostService.getPage(PostFilter(Types.PostStatus.PUBLISH.toString()), PageRequest.of(0, 10))
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("*** Start serialize List<AdminPostResponseDto> ***")
        println("Result Length: " + page.content.size)
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(page)

        println(result)
    }

    /**
     * Usage: CMS PostEditor, PostPreview
     *
     * @throws JsonProcessingException
     */
    @Test
    fun adminPostResponseDtoShouldSerializeCorrectly() {
        val dto = adminPostService.findOne(291L)
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("*** Start serialize AdminPostResponseDto ***")
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dto)

        println(result)
    }

}
