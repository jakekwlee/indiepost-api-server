package com.indiepost.service

import com.indiepost.NewIndiepostApplication
import com.indiepost.dto.CategoryDto
import com.indiepost.dto.ContributorDto
import com.indiepost.dto.TagDto
import com.indiepost.dto.post.Title
import com.indiepost.helper.printToJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class AdminServiceTests {
    @Inject
    private val service: AdminService? = null

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    fun buildInitialResponse_shouldReturnInitialDataProperly() {
        val dto = service!!.buildInitialResponse()
        assertThat(dto.authorNames)
                .isNotNull
                .hasAtLeastOneElementOfType(String::class.java)
        assertThat(dto.contributors)
                .isNotNull
                .hasAtLeastOneElementOfType(ContributorDto::class.java)
        assertThat(dto.categories)
                .isNotNull
                .hasAtLeastOneElementOfType(CategoryDto::class.java)
        assertThat(dto.currentUser).isNotNull
        assertThat(dto.tags)
                .isNotNull
                .hasAtLeastOneElementOfType(TagDto::class.java)
        assertThat(dto.postTitles)
                .isNotNull
                .hasAtLeastOneElementOfType(Title::class.java)
        printToJson(dto)
    }
}
