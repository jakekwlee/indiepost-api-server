package com.indiepost.dto.analytics

import javax.validation.constraints.NotNull

/**
 * Created by jake on 17. 4. 19.
 */
data class PageviewDto(

        @NotNull
        var path: String? = null,

        @NotNull
        var appName: String? = null,

        @NotNull
        var appVersion: String? = null,

        var referrer: String? = null,

        var userId: Long? = null,

        var postId: Long? = null
)
