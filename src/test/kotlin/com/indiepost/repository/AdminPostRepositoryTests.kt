package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import com.indiepost.dto.post.PostQuery
import com.indiepost.enums.Types.PostStatus.PUBLISH
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class AdminPostRepositoryTests {
    @Inject
    private lateinit var repository: AdminPostRepository


    @Test
    fun getTitleList_shouldReturnPostTitlesProperly() {
        val titleList = repository.getTitleList()
        val query = PostQuery.Builder(PUBLISH).build()
        assertThat(titleList).hasSize(repository.count(query)!!.toInt())
    }
}
