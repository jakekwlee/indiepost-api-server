package com.indiepost.dto.user

import java.time.Instant
import java.util.*

/**
 * Created by jake on 10/8/16.
 */
data class UserDto(
        var id: Long? = null,

        var username: String? = null,

        var displayName: String? = null,

        var email: String? = null,

        var joinedAt: Instant? = null,

        var updatedAt: Instant? = null,

        var profile: String? = null,

        var picture: String? = null,

        var gender: String? = null,

        var roles: MutableList<String>? = null,

        var roleType: String? = null
) {
    fun addRole(role: String) {
        if (this.roles == null) {
            this.roles = ArrayList()
        }
        this.roles!!.add(role)
    }
}

