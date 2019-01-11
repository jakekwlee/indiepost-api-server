package com.indiepost.dto.post

import com.indiepost.dto.Highlight
import java.time.Instant
import java.util.*

/**
 * Created by jake on 10/8/16.
 */
data class AdminPostSummaryDto(

        var id: Long? = null,

        var originalId: Long? = null,

        var title: String? = null,

        var status: String? = null,

        var displayName: String? = null,

        var categoryName: String? = null,

        var authorDisplayName: String? = null,

        var editorDisplayName: String? = null,

        var createdAt: Instant? = null,

        var publishedAt: Instant? = null,

        var modifiedAt: Instant? = null,

        var profiles: List<String> = ArrayList(),

        var tags: List<String> = ArrayList(),

        var highlight: Highlight? = null,

        var isFeatured: Boolean = false,

        var isPicked: Boolean = false,

        var isSplash: Boolean = false,

        var isBroken: Boolean = false,

        var likedCount: Int = 0
)
