package com.indiepost.dto.post

import com.indiepost.dto.ImageSetDto

import java.time.Instant

/**
 * Created by jake on 11/19/16.
 */
data class AdminPostResponseDto(
        var createdAt: Instant? = null,

        var modifiedAt: Instant? = null,

        var titleImage: ImageSetDto? = null,

        var originalStatus: String? = null,

        var postType: String? = null,

        var likesCount: Int = 0,

        var commentsCount: Int = 0,

        var authorId: Long? = null,

        var editorId: Long? = null,

        var authorName: String? = null,

        var editorName: String? = null
) : AdminPostRequestDto()
