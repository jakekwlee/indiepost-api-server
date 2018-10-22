package com.indiepost.dto.analytics

/**
 * Created by jake on 17. 4. 27.
 */
data class OverviewStats(

        var totalPageview: Long? = null,

        var totalUniquePageview: Long? = null,

        var totalUniquePostview: Long? = null,

        var totalPostview: Long? = null,

        var totalVisitor: Long? = null,

        var totalAppVisitor: Long? = null,

        var pageviewTrend: Trend? = null,

        var visitorTrend: Trend? = null,

        var totalUsers: Long? = null,

        var newSignups: Long? = null,

        var period: PeriodDto? = null
)
