package com.indiepost.dto

import com.indiepost.dto.post.Title
import com.indiepost.dto.user.UserDto

/**
 * Created by jake on 10/8/16.
 */
data class AdminInitialResponse(
        var authorNames: List<String>? = null,

        var tags: List<TagDto>? = null,

        var currentUser: UserDto? = null,

        var categories: List<CategoryDto>? = null,

        var contributors: List<ContributorDto>? = null,

        var profiles: List<ProfileSummaryDto>? = null,

        var postTitles: List<Title>? = null
)
