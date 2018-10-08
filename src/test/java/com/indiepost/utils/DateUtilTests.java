package com.indiepost.utils;

import com.indiepost.dto.analytics.TimeDomainStat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class DateUtilTests {
    @Test
    public void normalizeHoursOFTimeDomainStats_shouldReturnListOfTimeDomainStatsProperlyWhenPeriodIsADay() {
        List<TimeDomainStat> statList = new ArrayList<>();
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 9, 0, 0), 12L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 10, 0, 0), 100L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 11, 0, 0), 1L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 13, 0, 0), 222L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 14, 0, 0), 220L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 16, 0, 0), 22L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 17, 0, 0), 40L));

        List<TimeDomainStat> result = DateUtil.normalizeHoursOFTimeDomainStats(
                statList, LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 1)
        );
        assertThat(result).hasSize(24);
        assertThat(result.get(0).getStatDateTime().toLocalDate()
                .isEqual(statList.get(0).getStatDateTime().toLocalDate()))
                .isTrue();
        assertThat(result.get(result.size() - 1).getStatDateTime().toLocalDate()
                .isEqual(statList.get(statList.size() - 1).getStatDateTime().toLocalDate())
        ).isTrue();
    }

    @Test
    public void normalizeHoursOFTimeDomainStats_shouldReturnListOfTimeDomainStatsProperlyWhenPeriodIs3Days() {
        List<TimeDomainStat> statList = new ArrayList<>();
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 9, 0, 0), 12L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 10, 0, 0), 100L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 11, 0, 0), 1L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 13, 0, 0), 222L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 14, 0, 0), 220L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 16, 0, 0), 22L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 17, 0, 0), 40L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 19, 0, 0), 41L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 20, 0, 0), 20L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 21, 0, 0), 123L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 22, 0, 0), 393L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 1, 23, 0, 0), 11L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 2, 1, 0, 0), 233L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 2, 2, 0, 0), 20L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 2, 3, 0, 0), 1L));
        statList.add(new TimeDomainStat(LocalDateTime.of(2018, 1, 3, 1, 0, 0), 100L));

        List<TimeDomainStat> result = DateUtil.normalizeHoursOFTimeDomainStats(
                statList, LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 3)
        );
        assertThat(result).hasSize(72);
        assertThat(result.get(0).getStatDateTime().toLocalDate()
                .isEqual(statList.get(0).getStatDateTime().toLocalDate()))
                .isTrue();
        assertThat(result.get(result.size() - 1).getStatDateTime().toLocalDate()
                .isEqual(statList.get(statList.size() - 1).getStatDateTime().toLocalDate())
        ).isTrue();
    }
}
