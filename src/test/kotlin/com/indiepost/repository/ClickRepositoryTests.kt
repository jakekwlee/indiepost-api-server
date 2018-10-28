package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

/**
 * Created by jake on 8/10/17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(IndiepostBackendApplication::class))
@WebAppConfiguration
@Transactional
class ClickRepositoryTests {

    @Inject
    private lateinit var repository: ClickRepository

    @Test
    fun countAllClicksByCampaignId_shouldReturnExpectedValue() {
        val clicks = repository.countAllClicksByCampaignId(28L)
        assertThat(clicks).isGreaterThanOrEqualTo(218)
    }

    @Test
    fun countValidClicksByCampaignId_shouldReturnExpectedValue() {
        val clicks = repository.countValidClicksByCampaignId(28L)
        assertThat(clicks).isGreaterThanOrEqualTo(183)
    }
}
