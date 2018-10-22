package com.indiepost.dto.postcom.indiepost.dto.post

import java.util.*

data class RelatedPostsMatchingResult(
        var content: String? = null,

        var ids: List<Long> = ArrayList()
)

