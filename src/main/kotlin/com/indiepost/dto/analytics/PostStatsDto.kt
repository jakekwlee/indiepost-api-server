package com.indiepost.dto.analytics

import java.time.LocalDateTime

/**
 * Created by jake on 10/29/17.
 */
data class PostStatsDto(

        var lastUpdated: LocalDateTime? = null,

        var statData: List<PostStatDto>? = null,

        var period: PeriodDto? = null
) {
    constructor(lastUpdated: LocalDateTime, statData: List<PostStatDto>) : this() {
        this.lastUpdated = lastUpdated
        this.statData = statData
    }
}
