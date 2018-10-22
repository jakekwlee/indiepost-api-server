package com.indiepost.dto.analytics

import com.indiepost.enums.Types

import java.time.LocalDate

/**
 * Created by jake on 17. 4. 27.
 */
data class PeriodDto(

        var startDate: LocalDate? = null,

        var endDate: LocalDate? = null,

        var duration: Types.TimeDomainDuration? = null
) {
    constructor(startDate: LocalDate, endDate: LocalDate) : this() {
        this.startDate = startDate
        this.endDate = endDate
        this.duration = Types.TimeDomainDuration.DAILY
    }
}
