package com.indiepost.dto.analytics

import java.time.LocalDateTime

/**
 * Created by jake on 10/29/17.
 */
data class TimeDomainDoubleStat(
        var dateTime: LocalDateTime?,
        var value1: Long?,
        var value2: Long?
)
