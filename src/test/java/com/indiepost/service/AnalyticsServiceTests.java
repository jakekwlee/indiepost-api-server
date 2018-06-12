package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.analytics.OverviewStats;
import com.indiepost.dto.analytics.RecentAndOldPostStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static testHelper.JsonSerializer.printToJson;
import static testHelper.PeriodMaker.getDailyPeriod;

/**
 * Created by jake on 17. 4. 28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class AnalyticsServiceTests {

    @Inject
    private AnalyticsService analyticsService;

    @Test
    public void overviewStatsShouldSerializeCorrectly() {
        OverviewStats stats = analyticsService
                .getOverviewStats(getDailyPeriod());
        printToJson(stats);
    }

    @Test
    public void recentAndOldStatsShouldSerializeCorrectly() {
        RecentAndOldPostStats stats =
                analyticsService.getRecentAndOldPostStats(getDailyPeriod());
        printToJson(stats);
    }
}
