package com.indiepost.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.analytics.PeriodDto
import com.indiepost.dto.analytics.PostStatDto
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDate
import javax.inject.Inject

/**
 * Created by jake on 17. 4. 28.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
class AnalyticsServiceTests {

    @Inject
    private lateinit var analyticsService: AnalyticsService

    @Test
    @Throws(JsonProcessingException::class)
    fun overviewStatsShouldSerializeCorrectly() {
        val stats = analyticsService.getOverviewStats(
                PeriodDto(
                        LocalDate.of(2017, 4, 28),
                        LocalDate.of(2017, 4, 28)
                )
        )
        serializeAndPrintStats(stats, "Overview Stats")
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun recentAndOldStatsShouldSerializeCorrectly() {
        val stats = analyticsService.getRecentAndOldPostStats(
                PeriodDto(
                        LocalDate.of(2017, 8, 1),
                        LocalDate.of(2017, 8, 5)
                )
        )
        serializeAndPrintStats(stats, "Recent And Old Stats")
    }

    @Test
    fun getCachedPostStatsShouldWordProperly() {
        val postStatsDto = analyticsService.getCachedPostStats() ?: throw Exception()
        val stats = postStatsDto.statData
        val lastUpdated = postStatsDto.lastUpdated
        assertThat(postStatsDto).isNotNull()
        assertThat(lastUpdated).isNotNull()
        assertThat(stats).isNotNull().hasAtLeastOneElementOfType(PostStatDto::class.java)

        val dto = stats!![0]
        assertThat(dto).isNotNull()
        assertThat(dto.id).isNotNull()
        assertThat(dto.author).isNotEmpty()
        assertThat(dto.primaryTag).isNotEmpty()
        assertThat(dto.title).isNotEmpty()
        assertThat(dto.publishedAt).isNotNull()
        assertThat(dto.pageviews).isGreaterThanOrEqualTo(0)
        assertThat(dto.uniquePageviews).isGreaterThanOrEqualTo(0)
    }

    @Throws(JsonProcessingException::class)
    private fun serializeAndPrintStats(o: Any, name: String) {
        val objectMapper = ObjectMapper()
        println("\n\n*** Start serialize $name ***\n\n")
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(o)
        println("Size of results: " + result.toByteArray().size / 1024.0 + " kb")
        println(result)
    }

}
