package com.indiepost.dto.analytics

import java.time.LocalDateTime

/**
 * Created by jake on 17. 5. 10.
 */
data class PostStatDto(
        var id: Long? = null,

        var publishedAt: LocalDateTime? = null,

        var title: String? = null,

        var category: String? = null,

        var author: String? = null,

        var pageviews: Long? = null,

        var uniquePageviews: Long? = null
)
