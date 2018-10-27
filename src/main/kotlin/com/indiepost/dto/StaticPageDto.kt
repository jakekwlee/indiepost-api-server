package com.indiepost.dto

import com.indiepost.enums.Types

import java.time.Instant

/**
 * Created by jake on 17. 3. 5.
 */
data class StaticPageDto(

        var id: Long? = null,

        var title: String? = "No Title",

        var content: String? = "",

        var slug: String? = null,

        var displayOrder: Int = 999,

        var status: Types.PostStatus? = Types.PostStatus.DRAFT,

        var authorDisplayName: String? = null,

        var createdAt: Instant? = null,

        var modifiedAt: Instant? = null,

        var type: String = ""
)
