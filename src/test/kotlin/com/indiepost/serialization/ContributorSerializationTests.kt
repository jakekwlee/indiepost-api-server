package com.indiepost.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.indiepost.NewIndiepostApplication
import com.indiepost.enums.Types
import com.indiepost.service.ContributorService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
class ContributorSerializationTests {

    @Inject
    private lateinit var contributorService: ContributorService

    @Test
    fun pagedContributorDtoListShouldSerializeCorrectly() {
        val pageRequest = PageRequest(0, 10)
        val page = contributorService.find(Types.ContributorType.FeatureEditor, pageRequest)
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("*** Start serialize StaticPage<ContributorDto> ***")
        println("Result Length: " + page.content.size)
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(page)
        println(result)
    }

    @Test
    fun contributorDtoShouldSerializeCorrectly() {
        val dto = contributorService.findOne(1L)
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(Hibernate5Module())
        println("*** Start serialize ContributorDto ***")
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(dto)
        println(result)
    }
}
