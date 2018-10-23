package com.indiepost.repository

import com.fasterxml.jackson.core.JsonProcessingException
import com.indiepost.NewIndiepostApplication
import com.indiepost.enums.Types.ClientType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

/**
 * Created by jake on 17. 5. 25.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
@Transactional
class VisitorRepositoryTests {

    @Inject
    private lateinit var visitorRepository: VisitorRepository

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveTotalVisitors() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val result = visitorRepository.getTotalVisitors(since, until)

        printPeriod(dto)
        println("Total Visitors: " + result!!)
        assertThat(result.toLong()).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testRetrieveTotalMobileAppVisitors() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val result = visitorRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString())
        printPeriod(dto)
        println("Total Mobile App Visitors: " + result!!)
        assertThat(result.toLong()).isGreaterThan(0)
    }


    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopChannel() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = visitorRepository.getTopChannel(since, until, 10)
        testSerializeAndPrintStats(share, dto, "Top Channel")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopReferrer() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = visitorRepository.getTopReferrers(since, until, 10)
        testSerializeAndPrintStats(share, dto, "Top Referrer")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopOs() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = visitorRepository.getTopOs(since, until, 10)
        testSerializeAndPrintStats(share, dto, "Top Os")
        assertThat(share.size).isGreaterThan(0)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun testGetTopBrowsers() {
        val dto = getOneDayPeriod()
        val since = dto.startDate!!.atStartOfDay()
        val until = dto.endDate!!.atTime(23, 59, 59)
        val share = visitorRepository.getTopWebBrowsers(since, until, 10)
        testSerializeAndPrintStats(share, dto, "Top Web Browsers")
        assertThat(share.size).isGreaterThan(0)
    }
}
