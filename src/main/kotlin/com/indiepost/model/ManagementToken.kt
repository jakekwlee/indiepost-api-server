package com.indiepost.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Size

@Entity
data class ManagementToken(
        @Id
        var id: Int? = 1,

        @Size(max = 50)
        var provider: String? = "AUTH0",

        @Size(max = 5000)
        var accessToken: String? = null,

        var expireAt: LocalDateTime? = null
)
