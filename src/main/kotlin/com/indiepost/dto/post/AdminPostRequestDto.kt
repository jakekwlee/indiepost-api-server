package com.indiepost.dto.post

import java.time.Instant
import java.util.*

/**
 * Created by jake on 11/19/16.
 */
open class AdminPostRequestDto {

    var id: Long? = null

    var originalId: Long? = null

    var title: String? = null

    var content: String? = null

    var excerpt: String? = null

    var displayName: String? = null

    var titleImageId: Long? = null

    var status: String? = null

    var categoryId: Long? = null

    var publishedAt: Instant? = null

    var isFeatured: Boolean = false

    var isPicked: Boolean = false

    var isSplash: Boolean = false

    var isShowLastUpdated: Boolean = false

    var categoryName: String? = null

    var relatedPostIds: List<Long> = ArrayList()

    var tags: List<String> = ArrayList()

    var contributors: List<String> = ArrayList()
}
