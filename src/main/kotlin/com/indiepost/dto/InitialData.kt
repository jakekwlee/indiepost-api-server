package com.indiepost.dto

import com.indiepost.dto.analytics.BannerDto
import com.indiepost.dto.post.PostSummaryDto
import com.indiepost.dto.user.UserDto
import org.springframework.data.domain.Page

/**
 * Created by jake on 17. 1. 22.
 */
data class InitialData(
        var selectedTags: List<String>? = null,

        var currentUser: UserDto? = null,

        var posts: Page<PostSummaryDto>? = null,

        var topPosts: List<PostSummaryDto>? = null,

        var pickedPosts: List<PostSummaryDto>? = null,

        var splash: PostSummaryDto? = null,

        var featured: PostSummaryDto? = null,

        var staticPages: List<StaticPageDto>? = null,

        var banners: List<BannerDto>? = null,

        var isWithLatestPosts: Boolean = false
)
