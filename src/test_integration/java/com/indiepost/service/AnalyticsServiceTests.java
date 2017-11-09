package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.OverviewStats;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.RecentAndOldPostStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

/**
 * Created by jake on 17. 4. 28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AnalyticsServiceTests {
    @Autowired
    private AnalyticsService analyticsService;

    @Test
    public void overviewStatsShouldSerializeCorrectly() throws JsonProcessingException {
        OverviewStats stats = analyticsService.getOverviewStats(
                new PeriodDto(
                        LocalDate.of(2017, 4, 28),
                        LocalDate.of(2017, 4, 28)
                )
        );
        serializeAndPrintStats(stats, "Overview Stats");
    }

    @Test
    public void recentAndOldStatsShouldSerializeCorrectly() throws JsonProcessingException {
        RecentAndOldPostStats stats = analyticsService.getRecentAndOldPostStats(
                new PeriodDto(
                        LocalDate.of(2017, 8, 1),
                        LocalDate.of(2017, 8, 5)
                )
        );
        serializeAndPrintStats(stats, "Recent And Old Stats");
    }

    private void serializeAndPrintStats(Object o, String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("\n\n*** Start serialize " + name + " ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(o);
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println(result);
    }
}
