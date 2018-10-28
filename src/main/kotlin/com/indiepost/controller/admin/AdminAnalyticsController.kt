package com.indiepost.controller.admin

import com.indiepost.dto.analytics.*
import com.indiepost.service.AnalyticsService
import org.springframework.web.bind.annotation.*

import javax.inject.Inject

/**
 * Created by jake on 17. 4. 29.
 */
@RestController
@RequestMapping(value = ["/admin/stat"], produces = ["application/json; charset=UTF-8"])
class AdminAnalyticsController @Inject constructor(private val analyticsService: AnalyticsService) {

    @PostMapping
    fun getOverviewStats(@RequestBody periodDto: PeriodDto): OverviewStats {
        return analyticsService.getOverviewStats(periodDto)
    }

    @PostMapping("/tops")
    fun getTopStats(@RequestBody periodDto: PeriodDto): TopStats {
        return analyticsService.getTopStats(periodDto)
    }

    @PostMapping("/recent-and-old")
    fun getRecentAndOldPostStats(@RequestBody periodDto: PeriodDto): RecentAndOldPostStats {
        return analyticsService.getRecentAndOldPostStats(periodDto)
    }

    @GetMapping("/posts")
    fun getAllPostStats(): PostStatsDto? {
        return analyticsService.getCachedPostStats()
    }

    @PostMapping("/posts")
    fun getPostStats(@RequestBody periodDto: PeriodDto): PostStatsDto {
        return analyticsService.getPostStats(periodDto)
    }
}
