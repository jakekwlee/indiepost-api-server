package com.indiepost.repository

import com.indiepost.enums.Types
import com.indiepost.model.CachedAnalyticsData
import java.time.LocalDate

interface CachedAnalyticsDataRepository {
    fun save(stats: CachedAnalyticsData)

    fun <T> find(startDate: LocalDate,
                 endDate: LocalDate,
                 statsType: Types.CachedStatsType = Types.CachedStatsType.Overview,
                 filterType: Types.CachedStatsFilterType = Types.CachedStatsFilterType.NoFilter,
                 filterValue: String? = null): T?
}