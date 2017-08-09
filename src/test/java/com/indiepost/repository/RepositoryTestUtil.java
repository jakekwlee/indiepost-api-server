package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.TimeDomainStat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface RepositoryTestUtil {
    static PeriodDto getYearlyPeriod() {
        LocalDate since = LocalDate.of(2015, 4, 20);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }

    static PeriodDto getMonthlyPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 25);
        LocalDate until = LocalDate.of(2017, 5, 10);
        return new PeriodDto(since, until);
    }

    static PeriodDto getDailyPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 24);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }

    static PeriodDto getOneDayPeriod() {
        LocalDate since = LocalDate.of(2017, 4, 26);
        LocalDate until = LocalDate.of(2017, 4, 26);
        return new PeriodDto(since, until);
    }


    static void testSerializeAndPrintStats(List list, PeriodDto dto, String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("\n\n*** Start serialize " + name + " ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(list);
        printPeriod(dto);
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println("Length of results: " + list.size());
        System.out.println(result);
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
