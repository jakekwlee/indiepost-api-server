package com.indiepost.model.elasticsearch

import io.searchbox.annotations.JestId
import java.io.Serializable
import java.util.*

data class PostEs(
        @JestId
        var id: Long? = null,

        var title: String? = null,

        var excerpt: String? = null,

        var content: String? = null,

        var bylineName: String? = null,

        var profiles: MutableList<String> = ArrayList(),

        var tags: MutableList<String> = ArrayList(),

        var status: String? = null,

        var creatorId: Long? = null,

        var modifiedUserId: Long? = null,

        var creatorName: String? = null,

        var modifiedUserName: String? = null,

        var categoryName: String? = null

) : Serializable {
    companion object {
        private const val serialVersionUID = 6587432935142567307L
    }
}
