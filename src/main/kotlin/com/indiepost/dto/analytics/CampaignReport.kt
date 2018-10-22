package com.indiepost.dto.analytics

data class CampaignReport(
        var campaign: CampaignDto? = null,

        var trend: List<TimeDomainStat>? = null,

        var byOS: List<ShareStat>? = null,

        var byLinks: List<ShareStat>? = null,

        var byBrowsers: List<ShareStat>? = null,

        var topPrevious: List<ShareStat>? = null,

        var rawData: List<RawDataReportRow>? = null
)
