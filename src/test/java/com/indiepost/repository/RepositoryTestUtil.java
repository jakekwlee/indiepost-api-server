package com.indiepost.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.dto.analytics.PeriodDto;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.model.analytics.Campaign;
import com.indiepost.model.analytics.Click;
import com.indiepost.model.analytics.Link;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface RepositoryTestUtil {
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

    static Campaign newTestCampaign() {
        Campaign campaign = new Campaign();
        campaign.setName("test campaign");
        campaign.setClientName("test client");
        campaign.setGoal(1000L);
        campaign.setStartAt(LocalDateTime.now().minusHours(10L));
        campaign.setEndAt(LocalDateTime.now().plusHours(10L));
        return campaign;
    }

    static Link newTestLink() {
        Link link = new Link();
        link.setName("test link");
        link.setUrl("https://google.com");
        link.setUid("aa332211");
        link.setCreatedAt(LocalDateTime.now());
        return link;
    }

    static Click newTestClick() {
        Click click = new Click();
        click.setPath("http://www.indiepost.co.kr/link/ava");
        click.setTimestamp(LocalDateTime.now());
        return click;
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
