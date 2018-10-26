package com.indiepost.service

import com.indiepost.dto.analytics.*

/**
 * Created by jake on 17. 4. 13.
 */
interface AnalyticsService {

    fun getCachedPostStats(): PostStatsDto?

    fun getOverviewStats(periodDto: PeriodDto): OverviewStats

    fun getRecentAndOldPostStats(periodDto: PeriodDto): RecentAndOldPostStats

    fun getPostStats(periodDto: PeriodDto): PostStatsDto

    fun getTopStats(periodDto: PeriodDto): TopStats
}
