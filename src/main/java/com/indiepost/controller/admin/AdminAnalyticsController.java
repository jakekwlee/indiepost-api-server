package com.indiepost.controller.admin;

import com.indiepost.dto.stat.*;
import com.indiepost.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jake on 17. 4. 29.
 */
@RestController
@RequestMapping(value = "/admin/stat", produces = {"application/json; charset=UTF-8"})
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AdminAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping
    public OverviewStats getOverviewStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getOverviewStats(periodDto);
    }

    @PostMapping("/tops")
    public TopStats getTopStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getTopStats(periodDto);
    }

    @PostMapping("/recent-and-old")
    public RecentAndOldPostStats getRecentAndOldPostStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getRecentAndOldPostStats(periodDto);
    }

    @GetMapping("/posts")
    public PostStatsDto getAllPostStats() {
        return analyticsService.getCachedPostStats();
    }

    @PostMapping("/posts")
    public PostStatsDto getPostStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getPostStats(periodDto);
    }
}
