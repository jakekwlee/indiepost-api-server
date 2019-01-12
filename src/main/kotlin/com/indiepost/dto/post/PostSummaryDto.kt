package com.indiepost.dto.post

import com.indiepost.dto.Highlight
import com.indiepost.dto.ImageSetDto
import com.indiepost.enums.Types.PostStatus
import java.io.Serializable
import java.time.Instant

/**
 * Created by jake on 17. 1. 21.
 */
open class PostSummaryDto : Serializable {

    var id: Long? = null

    var isFeatured: Boolean = false

    var isPicked: Boolean = false

    var isSplash: Boolean = false

    var title: String? = null

    var excerpt: String? = null

    var displayName: String? = null

    var publishedAt: Instant? = null

    var modifiedAt: Instant? = null

    var isShowLastUpdated: Boolean = false

    var titleImage: ImageSetDto? = null

    var titleImageId: Long? = null

    var status: PostStatus? = null

    var primaryTag: String? = null

    var commentsCount: Int = 0

    var lastRead: Instant? = null

    var bookmarked: Instant? = null

    var isInteractionFetched: Boolean = false

    var highlight: Highlight? = null

}
