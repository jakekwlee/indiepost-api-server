package com.indiepost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.analytics.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jake on 17. 4. 28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AnalyticsServiceTest {

    @Inject
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

    @Test
    public void getCachedPostStatsShouldWordProperly() {
        PostStatsDto postStatsDto = analyticsService.getCachedPostStats();
        List<PostStatDto> stats = postStatsDto.getStatData();
        LocalDateTime lastUpdated = postStatsDto.getLastUpdated();
        assertThat(postStatsDto).isNotNull();
        assertThat(lastUpdated).isNotNull();
        assertThat(stats).isNotNull().hasAtLeastOneElementOfType(PostStatDto.class);

        PostStatDto dto = stats.get(0);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getAuthor()).isNotEmpty();
        assertThat(dto.getCategory()).isNotEmpty();
        assertThat(dto.getTitle()).isNotEmpty();
        assertThat(dto.getPublishedAt()).isNotNull();
        assertThat(dto.getPageviews()).isGreaterThanOrEqualTo(0);
        assertThat(dto.getLegacyPageviews()).isGreaterThanOrEqualTo(0);
        assertThat(dto.getUniquePageviews()).isGreaterThanOrEqualTo(0);
        assertThat(dto.getLegacyUniquePageviews()).isGreaterThanOrEqualTo(0);
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
