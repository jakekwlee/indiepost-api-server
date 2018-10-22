package com.indiepost.dto

import java.time.Instant

data class ImageSetDto(
        var id: Long? = null,

        var width: Int = 0,

        var height: Int = 0,

        var uploadedAt: Instant? = null,

        var contentType: String? = null,

        var original: String? = null,

        var large: String? = null,

        var optimized: String? = null,

        var small: String? = null,

        var thumbnail: String? = null
)
