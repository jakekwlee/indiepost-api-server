package com.indiepost.dto.user

data class SyncAuthorizationResponse(
        val isNewUser: Boolean,

        val user: UserDto
)
