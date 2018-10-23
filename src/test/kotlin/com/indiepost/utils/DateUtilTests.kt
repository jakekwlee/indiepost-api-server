package com.indiepost.utils

import com.indiepost.dto.analytics.TimeDomainStat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class DateUtilTests {
    @Test
    fun normalizeHoursOFTimeDomainStats_shouldReturnListOfTimeDomainStatsProperlyWhenPeriodIsADay() {
        val statList = ArrayList<TimeDomainStat>()
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 9, 0, 0), 12L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 10, 0, 0), 100L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 11, 0, 0), 1L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 13, 0, 0), 222L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 14, 0, 0), 220L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 16, 0, 0), 22L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 17, 0, 0), 40L))

        val result = DateUtil.normalizeHoursOFTimeDomainStats(
                statList, LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 1)
        )
        assertThat(result).hasSize(24)
        assertThat(result[0].statDateTime!!.toLocalDate()
                .isEqual(statList[0].statDateTime!!.toLocalDate()))
                .isTrue()
        assertThat(result[result.size - 1].statDateTime!!.toLocalDate()
                .isEqual(statList[statList.size - 1].statDateTime!!.toLocalDate())
        ).isTrue()
    }

    @Test
    fun normalizeHoursOFTimeDomainStats_shouldReturnListOfTimeDomainStatsProperlyWhenPeriodIs3Days() {
        val statList = ArrayList<TimeDomainStat>()
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 9, 0, 0), 12L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 10, 0, 0), 100L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 11, 0, 0), 1L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 13, 0, 0), 222L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 14, 0, 0), 220L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 16, 0, 0), 22L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 17, 0, 0), 40L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 19, 0, 0), 41L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 20, 0, 0), 20L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 21, 0, 0), 123L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 22, 0, 0), 393L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 1, 23, 0, 0), 11L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 2, 1, 0, 0), 233L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 2, 2, 0, 0), 20L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 2, 3, 0, 0), 1L))
        statList.add(TimeDomainStat(LocalDateTime.of(2018, 1, 3, 1, 0, 0), 100L))

        val result = DateUtil.normalizeHoursOFTimeDomainStats(
                statList, LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 3)
        )
        assertThat(result).hasSize(72)
        assertThat(result[0].statDateTime!!.toLocalDate()
                .isEqual(statList[0].statDateTime!!.toLocalDate()))
                .isTrue()
        assertThat(result[result.size - 1].statDateTime!!.toLocalDate()
                .isEqual(statList[statList.size - 1].statDateTime!!.toLocalDate())
        ).isTrue()
    }
}
