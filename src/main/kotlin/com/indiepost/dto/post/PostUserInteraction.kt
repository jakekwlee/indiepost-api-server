package com.indiepost.dto.post

import java.time.Instant

data class PostUserInteraction(
        var postId: Long? = null,

        var lastRead: Instant? = null,

        var bookmarked: Instant? = null
) {
    constructor(postId: Long) : this() {
        this.postId = postId
    }
}

