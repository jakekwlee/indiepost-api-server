package com.indiepost.dto.analytics

import java.time.LocalDateTime

/**
 * Created by jake on 17. 4. 24.
 */
class TimeDomainStat {

    var statDateTime: LocalDateTime? = null

    var statValue: Long? = null

    constructor() {}

    constructor(statDateTime: LocalDateTime, statValue: Long?) {
        this.statDateTime = statDateTime
        this.statValue = statValue
    }
}
