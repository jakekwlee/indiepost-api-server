package com.indiepost.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.utils.DateUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jake on 17. 4. 28.
 */
class AnalyticsServiceUnitTests {

    @Test
    @Throws(JsonProcessingException::class)
    fun normalizeTimeDomainStatsWorksCorrectly() {

        val input = ArrayList<TimeDomainStat>()
        val d1 = LocalDateTime.of(2017, 1, 1, 4, 0, 0)
        val d2 = LocalDateTime.of(2017, 1, 1, 10, 0, 0)

        val d1Value = 1000L
        val d2Value = 3000L

        input.add(TimeDomainStat(d1, d1Value))
        input.add(TimeDomainStat(d2, d2Value))
        val output = DateUtil.normalizeTimeDomainStats(
                input,
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1))
        assertThat(output.size).isEqualTo(24)
        for (i in output.indices) {
            val stat = output[i]
            when (i) {
                4 -> assertThat(stat.statValue).isEqualTo(d1Value)
                10 -> assertThat(stat.statValue).isEqualTo(d2Value)
                else -> assertThat(stat.statValue).isEqualTo(0L)
            }
        }
    }
}
