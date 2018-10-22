package com.indiepost.dto

import com.indiepost.model.ImageSet

/**
 * Created by jake on 10/12/17.
 */
data class PostImageSetDto(
        var titleImage: ImageSet? = null,

        var images: List<ImageSet>? = null
)
