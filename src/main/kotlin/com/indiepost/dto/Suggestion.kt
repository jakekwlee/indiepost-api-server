package com.indiepost.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 8/31/17.
 */
data class Suggestion(

        var userId: Long? = null,

        @NotNull
        @Size(min = 1, max = 200)
        var subject: String? = null,

        @NotNull
        @Size(min = 10, max = 10000)
        var content: String? = null,

        @NotNull
        @Size(min = 1, max = 100)
        var proposer: String? = null,

        @NotNull
        @Size(min = 1, max = 100)
        var email: String? = null,

        var contact: String? = null
)
