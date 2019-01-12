package com.indiepost.dto.analytics

data class TopStats(
        var period: PeriodDto? = null,

        var pageviewByPrimaryTag: List<ShareStat>? = null,

        var pageviewByAuthor: List<ShareStat>? = null,

        var topPagesWebapp: List<ShareStat>? = null,

        var topPagesMobile: List<ShareStat>? = null,

        var topPosts: List<ShareStat>? = null,

        var topPostsMobile: List<ShareStat>? = null,

        var topPostsWebapp: List<ShareStat>? = null,

        var topReferrer: List<ShareStat>? = null,

        var topBrowser: List<ShareStat>? = null,

        var topOs: List<ShareStat>? = null,

        var topTags: List<ShareStat>? = null,

        var topChannel: List<ShareStat>? = null
)
