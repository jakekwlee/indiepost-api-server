package com.indiepost.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity
data class ManagementToken(
        @Id
        var id: Int? = null,

        @Size(max = 50)
        var provider: String? = null,

        @Size(max = 5000)
        var accessToken: String? = null,

        var expireAt: LocalDateTime? = null
) {
    constructor(accessToken: String, expireAt: LocalDateTime) : this() {
        this.id = 1
        this.accessToken = accessToken
        this.expireAt = expireAt
        this.provider = "AUTH0"
    }
}
