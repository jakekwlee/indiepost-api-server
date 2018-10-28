package com.indiepost.dto.analytics

/**
 * Created by jake on 17. 4. 21.
 */
data class ActionDto(
        var appName: String? = null,

        var appVersion: String? = null,

        var path: String? = null,

        var actionType: String? = null,

        var label: String? = null,

        var value: Int? = 1,

        var userId: Long? = null
)


