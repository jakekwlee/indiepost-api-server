package com.indiepost.dto.analytics

data class BannerDto(
        var id: Long? = null,

        var title: String? = null,

        var subtitle: String? = null,

        var bannerType: String? = null,

        var bgColor: String = "#ccc",

        var imageUrl: String? = null,

        var linkTo: String? = null,

        var internalUrl: String? = null,

        var isCover: Boolean = false,

        var target: String? = null,

        var priority: Int = 0
)
