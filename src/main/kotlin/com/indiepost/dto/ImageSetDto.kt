package com.indiepost.dto

import java.time.Instant

data class ImageSetDto(
        var id: Long? = null,

        var uploadedAt: Instant? = null,

        var contentType: String? = null,

        var original: ImageDto? = null,

        var large: ImageDto? = null,

        var optimized: ImageDto? = null,

        var small: ImageDto? = null,

        var thumbnail: ImageDto? = null
)
