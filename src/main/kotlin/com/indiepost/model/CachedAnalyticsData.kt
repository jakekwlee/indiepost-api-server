package com.indiepost.model

import com.indiepost.enums.Types.*
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

//@Entity
//@Table(name = "CachedAnalyticsData")
data class CachedAnalyticsData(
        @Id
        @GeneratedValue
        val id: Long? = null,

        var startDate: LocalDate? = null,

        var endDate: LocalDate? = null,

        var created: LocalDateTime? = LocalDateTime.now(),

        var duration: TimeDomainDuration? = null,

        @Column(nullable = false, length = 12)
        var filterType: CachedStatsFilterType = CachedStatsFilterType.NoFilter,

        @Column(nullable = false, length = 12)
        var statsType: CachedStatsType = CachedStatsType.Overview,

        var filterValue: String? = null,

        @Column(nullable = false, columnDefinition = "LONGTEXT")
        var serializedData: String? = null
)