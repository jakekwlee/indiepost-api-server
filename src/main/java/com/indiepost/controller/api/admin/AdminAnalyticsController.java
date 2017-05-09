package com.indiepost.controller.api.admin;

import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.SiteStats;
import com.indiepost.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public SiteStats getSiteStats(@RequestBody PeriodDto periodDto) {
        return analyticsService.getStats(periodDto);
    }
}
