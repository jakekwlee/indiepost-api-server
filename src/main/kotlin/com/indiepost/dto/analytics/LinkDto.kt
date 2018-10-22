package com.indiepost.dto.analytics

import com.indiepost.enums.Types.LinkType
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 8/10/17.
 */
data class LinkDto(

        var id: Long? = null,

        @NotNull
        @Size(min = 2, max = 30)
        var name: String? = null,

        @NotNull
        @URL
        var url: String? = null,

        @NotNull
        var campaignId: Long? = null,

        @NotNull
        @Size(min = 8, max = 30)
        var uid: String? = null,

        @NotNull
        var linkType: String = LinkType.Standard.toString(),

        var banner: BannerDto? = null,

        var allClicks: Long? = null,

        var validClicks: Long? = null,

        var createdAt: LocalDateTime? = null,

        var isUpdated: Boolean = false
)
