package com.indiepost.dto.analytics

import com.indiepost.enums.Types.TimeDomainDuration

/**
 * Created by jake on 10/29/17.
 */
data class DoubleTrend(
        var statName: String? = null,

        var duration: TimeDomainDuration? = null,

        var result: List<TimeDomainDoubleStat>? = null
)
