package com.indiepost.dto.user

data class UserProfileDto(
        var id: Long? = null,

        var username: String? = null,

        var displayName: String? = null,

        var email: String? = null
)
