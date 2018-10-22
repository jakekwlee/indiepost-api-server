package com.indiepost.dto.analytics

import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 8/10/17.
 */
data class CampaignDto(

        var id: Long? = null,

        @NotNull
        @Size(min = 1, max = 100)
        var clientName: String? = null,

        @NotNull
        @Size(min = 1, max = 100)
        var name: String? = null,

        @NotNull
        var startAt: Instant? = null,

        @NotNull
        var endAt: Instant? = null,

        var createdAt: LocalDateTime? = null,

        @NotNull
        var goal: Long? = 0,

        var validClicks: Long? = 0,

        var allClicks: Long? = 0,

        var links: List<LinkDto> = ArrayList()
)
