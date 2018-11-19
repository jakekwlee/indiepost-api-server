package com.indiepost.dto

import java.time.Instant

data class ProfileDto(
        var id: Long? = null,

        var fullName: String? = null,

        var displayName: String? = null,

        var slug: String? = null,

        var email: String = "email@example.com",

        var showEmail: Boolean = false,

        var subEmail: String? = null,

        var label: String = "Writer",

        var showLabel: Boolean = true,

        var description: String? = null,

        var showDescription: Boolean = false,

        var profileType: String? = null,

        var profileState: String? = null,

        var etc: String? = null,

        var created: Instant? = null,

        var lastUpdated: Instant? = null
)
