package com.indiepost.serialization

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.indiepost.IndiepostBackendApplication
import com.indiepost.service.ImageService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 22.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class ImageSetSerializationTest {

    @Inject
    private lateinit var imageService: ImageService

    /**
     * Usage: CMS MediaExplorer
     *
     * @throws JsonProcessingException
     */
    @Test
    @Throws(JsonProcessingException::class)
    fun imageSetShouldSerializeCorrectly() {
        val page = imageService.findAll(PageRequest.of(2, 10))
        val imageSetList = page.content
        val objectMapper = ObjectMapper()
        println("*** Start serialize List<ImageSet> ***")
        println("Result Length: " + imageSetList.size)
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(imageSetList)

        println(result)
    }
}
