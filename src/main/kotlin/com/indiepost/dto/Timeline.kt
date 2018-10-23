package com.indiepost.dto

import java.time.Instant

class Timeline<T> {
    val content: List<T>

    val numberOfElements: Int

    val totalElements: Int

    val isLast: Boolean

    val size: Int

    val isAfter: Boolean

    val oldest: Instant?

    val newest: Instant?

    val timepoint: Instant

    constructor(content: List<T>, request: TimelineRequest, total: Int) {
        this.content = content
        this.totalElements = total
        this.numberOfElements = content.size
        this.isLast = total == content.size
        this.size = request.size
        this.isAfter = request.isAfter
        this.timepoint = Instant.ofEpochSecond(request.timepoint)
        this.oldest = null
        this.newest = null
    }

    constructor(content: List<T>, request: TimelineRequest, newest: Instant, oldest: Instant, total: Int) {
        this.content = content
        this.totalElements = total
        this.numberOfElements = content.size
        this.isLast = total == content.size
        this.size = request.size
        this.isAfter = request.isAfter
        this.oldest = oldest
        this.newest = newest
        this.timepoint = Instant.ofEpochSecond(request.timepoint)
    }
}
