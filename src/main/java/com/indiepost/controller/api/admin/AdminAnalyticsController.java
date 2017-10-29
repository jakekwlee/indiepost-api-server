package com.indiepost.controller.api.admin;

import com.indiepost.dto.stat.*;
import com.indiepost.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 17. 4. 29.
 */
@RestController
@RequestMapping(value = "/api/admin/stat", produces = {"application/json; charset=UTF-8"})
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

    @PostMapping("/recent-and-old")
    public RecentAndOldPostStats getRecentAndOldPostStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getRecentAndOldPostStats(periodDto);
    }

    @GetMapping("/posts")
    public PostStatsDto getAllPostStats() {
        return analyticsService.getAllPostStats();
    }

    @PostMapping("/posts")
    public List<PostStatDto> getPostStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getPostStats(periodDto);
    }
}
