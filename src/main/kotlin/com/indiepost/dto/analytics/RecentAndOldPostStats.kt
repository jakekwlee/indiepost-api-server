package com.indiepost.dto.analytics

/**
 * Created by jake on 10/29/17.
 */
data class RecentAndOldPostStats(
        var trend: DoubleTrend? = null,

        var period: PeriodDto? = null,

        var topOldPosts: List<ShareStat>? = null,

        var topRecentPosts: List<ShareStat>? = null
)
