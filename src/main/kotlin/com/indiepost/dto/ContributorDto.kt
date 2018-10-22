package com.indiepost.dto

import java.time.LocalDateTime

data class ContributorDto(
        var id: Long? = null,

        var fullName: String? = null,

        var email: String? = null,

        var subEmail: String? = null,

        var title: String? = null,

        var description: String? = null,

        var url: String? = null,

        var phone: String? = null,

        var picture: String? = null,

        var displayType: String? = null,

        var contributorType: String? = null,

        var etc: String? = null,

        var priority: Int = 0,

        var isTitleVisible: Boolean = false,

        var isEmailVisible: Boolean = false,

        var isDescriptionVisible: Boolean = false,

        var isUrlVisible: Boolean = false,

        var isPictureVisible: Boolean = false,

        var isPhoneVisible: Boolean = false,

        var lastUpdated: LocalDateTime? = null,

        var created: LocalDateTime? = null
)
