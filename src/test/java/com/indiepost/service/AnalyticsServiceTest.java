package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.SiteStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AnalyticsServiceTest {
    @Autowired
    private AnalyticsService analyticsService;

    @Test
    public void siteStatsShouldSerializeCorrectly() throws JsonProcessingException {
        SiteStats stats = analyticsService.getStats(
                new PeriodDto(
                        LocalDateTime.of(2017, 4, 20, 0, 0, 0),
                        LocalDateTime.of(2017, 4, 28, 23, 59, 59)
                )
        );
        serializeAndPrintStats(stats, "Site Stats");
    }

    private void serializeAndPrintStats(SiteStats siteStats, String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("\n\n*** Start serialize " + name + " ***\n\n");
        String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(siteStats);
        System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
        System.out.println(result);
    }
}
