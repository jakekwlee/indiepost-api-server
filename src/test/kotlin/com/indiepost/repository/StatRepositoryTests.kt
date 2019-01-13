package com.indiepost.repository

import com.fasterxml.jackson.core.JsonProcessingException
import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.analytics.PeriodDto
import com.indiepost.enums.Types.ClientType
import com.indiepost.enums.Types.TimeDomainDuration
import com.indiepost.helper.printToJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject
import javax.transaction.Transactional

/**
 * Created by jake on 8/9/17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(IndiepostBackendApplication::class))
@WebAppConfiguration
@Transactional
class StatRepositoryTests {

    @Inject
    private lateinit var statRepository: StatRepository

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveYearlyPageviewTrend() {
        val dto = getYearlyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.YEARLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Pageview Trend")
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveMonthlyPageviewTrend() {
        val dto = getMonthlyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.MONTHLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend")
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveDailyPageviewTrend() {
        val dto = getDailyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.DAILY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend")
        assertThat(pageviewTrend.size).isEqualTo(5)
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isNotEqualTo(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveOneDayPageviewTrend() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getPageviewTrend(since, until, TimeDomainDuration.HOURLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Pageview Trend")
        assertThat(pageviewTrend.size).isEqualTo(24)
        assertThat(sumOfTimeDomainStat(pageviewTrend)).isEqualTo(2184)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveYearlyOldAndNewPageviewTrend() {
        val dto = getYearlyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.YEARLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Yearly Old And New Pageview Trend")
        val totalPageviewExpected = statRepository.getTotalPostviews(since, until)
        val totalPageviewActual = pageviewTrend.stream()
                .map<Long> { (_, value1) -> value1 }
                .mapToLong { value -> value!!.toLong() }
                .sum()
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveMonthlyOldAndNewPageviewTrend() {
        val dto = getMonthlyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.MONTHLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Old And New Pageview Trend")
        val totalPageviewExpected = statRepository.getTotalPostviews(since, until)
        val totalPageviewActual = pageviewTrend.stream()
                .map<Long> { (_, value1) -> value1 }
                .mapToLong { value -> value!!.toLong() }
                .sum()
        testSerializeAndPrintStats(pageviewTrend, dto, "Monthly Pageview Trend")
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveDailyOldAndNewPageviewTrend() {
        val dto = getDailyPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.DAILY)
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Old And New Pageview Trend")
        val totalPageviewExpected = statRepository.getTotalPostviews(since, until)
        val totalPageviewActual = pageviewTrend.stream()
                .map<Long> { (_, value1) -> value1 }
                .mapToLong { value -> value!!.toLong() }
                .sum()
        testSerializeAndPrintStats(pageviewTrend, dto, "Daily Pageview Trend")
        assertThat(pageviewTrend.size).isEqualTo(5)
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveOneDayOldAndNewPageviewTrend() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val pageviewTrend = statRepository.getRecentAndOldPageviewTrend(since, until, TimeDomainDuration.HOURLY)
        testSerializeAndPrintStats(pageviewTrend, dto, "One Day Old And New Pageview Trend")
        val totalPageviewExpected = statRepository.getTotalPostviews(since, until)
        val totalPageviewActual = pageviewTrend.stream()
                .map<Long> { (_, value1) -> value1 }
                .mapToLong { value -> value!!.toLong() }
                .sum()
        assertThat(totalPageviewActual).isEqualTo(totalPageviewExpected)
        assertThat(pageviewTrend.size).isEqualTo(24)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveYearlyTotalPageview() {
        val expected = 306742L
        val dto = getYearlyPeriod()
        val result = testRetrieveTotals(dto)
        assertThat(result).isGreaterThanOrEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveMonthlyTotalPageview() {
        val expected = 234267L
        val dto = getMonthlyPeriod()
        val result = testRetrieveTotals(dto)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveDailyTotalPageview() {
        val expected = 13405L
        val dto = getDailyPeriod()
        val result = testRetrieveTotals(dto)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveOneDayTotalPageview() {
        val expected = 2184L
        val dto = getOneDayPeriod()
        val result = testRetrieveTotals(dto)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveTotalPostview() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val result = statRepository.getTotalPageviews(since, until)

        printPeriod(dto)
        println("Total Postviews: $result")
        assertThat(result).isEqualTo(2184)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetPageviewsByPrimaryTag() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getPageviewsByPrimaryTag(since, until, 30)
        testSerializeAndPrintStats(share, dto, "Pageview By Primary Tag")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetPageviewsByAuthor() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getPageviewsByAuthor(since, until, 30)
        testSerializeAndPrintStats(share, dto, "Pageview By Author")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopPagesWebapp() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopPages(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString())
        testSerializeAndPrintStats(share, dto, "Top Pages (Webapp)")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopPagesMobile() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopPages(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString())
        testSerializeAndPrintStats(share, dto, "Top pages (Mobile)")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopPostWebapp() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopPosts(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString())
        testSerializeAndPrintStats(share, dto, "Top Posts (Webapp)")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopPostMobile() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopPosts(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString())
        testSerializeAndPrintStats(share, dto, "Top Posts (Mobile)")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopLadingPagesWebapp() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopLandingPages(since, until, 10, ClientType.INDIEPOST_WEBAPP.toString())
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Webapp)")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopLadingPagesMobile() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopLandingPages(since, until, 10, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString())
        testSerializeAndPrintStats(share, dto, "Top Landing Pages (Mobile)")
        assertThat(share.size).isGreaterThan(0)
    }

    fun testRetrieveTotals(dto: PeriodDto): Long? {
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val result = statRepository.getTotalPageviews(since, until)
        println("Date since: $since")
        println("Date until: $until")
        println("Total Pageviews: $result")
        return result
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopTags() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = statRepository.getTopTags(since, until, 10)
        testSerializeAndPrintStats(share, dto, "Top Tags")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun getPostCountsByPrimaryTags_shouldReturnStatsProperly() {
        val result = statRepository.getPostCountsByPrimaryTags(
                LocalDateTime.of(2018, Month.DECEMBER, 1, 0, 0), LocalDateTime.of(2019, Month.JANUARY, 1, 1, 0))
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(9)
        printToJson(result)
    }
}
