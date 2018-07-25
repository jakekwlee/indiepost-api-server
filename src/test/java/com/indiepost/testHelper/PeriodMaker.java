package com.indiepost.testHelper;

import com.indiepost.dto.analytics.PeriodDto;
import com.indiepost.dto.analytics.TimeDomainStat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface PeriodMaker {

    static PeriodDto getYearlyPeriod() {
        LocalDate since = LocalDate.of(2015, 4, 20);
        LocalDate until = LocalDate.of(2017, 8, 31);
        return new PeriodDto(since, until);
    }

    static PeriodDto getMonthlyPeriod() {
        LocalDate since = LocalDate.of(2017, 6, 1);
        LocalDate until = LocalDate.of(2017, 8, 31);
        return new PeriodDto(since, until);
    }

    static PeriodDto getDailyPeriod() {
        LocalDate since = LocalDate.of(2017, 8, 1);
        LocalDate until = LocalDate.of(2017, 8, 5);
        return new PeriodDto(since, until);
    }

    static PeriodDto getOneDayPeriod() {
        LocalDate since = LocalDate.of(2017, 8, 5);
        LocalDate until = LocalDate.of(2017, 8, 5);
        return new PeriodDto(since, until);
    }

    static void printPeriod(PeriodDto dto) {
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(23, 59, 59);
        System.out.println("Date since: " + since);
        System.out.println("Date until: " + until);
    }

    static int sumOfTimeDomainStat(List<TimeDomainStat> timeDomainStats) {
        return timeDomainStats.stream()
                .map(TimeDomainStat::getStatValue)
                .mapToInt(Long::intValue)
                .sum();
    }
}
