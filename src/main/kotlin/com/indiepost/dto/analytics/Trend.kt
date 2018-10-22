package com.indiepost.dto.analytics

import com.indiepost.enums.Types.TimeDomainDuration

/**
 * Created by jake on 17. 5. 24.
 */
data class Trend(
        var duration: TimeDomainDuration? = null,

        var result: List<TimeDomainStat>? = null
)
