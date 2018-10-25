package com.indiepost.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 9/7/17.
 */
data class Inquiry(
        var userId: Long? = null,

        @NotNull
        @Size(max = 50)
        var inquirer: String? = null,

        @NotNull
        var email: String? = null,

        @Size(max = 50)
        var contact: String? = null,

        @Size(max = 50)
        var clientName: String? = null,

        var url: String? = null,

        @NotNull
        @Size(min = 1, max = 3000)
        var content: String? = null
)
