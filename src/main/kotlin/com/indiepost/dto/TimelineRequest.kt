package com.indiepost.dto

data class TimelineRequest(
        var timepoint: Long = 0,

        var isAfter: Boolean = false,

        var size: Int = 0

) {
    constructor(timepoint: Long, size: Int) : this() {
        this.timepoint = timepoint
        this.size = size
    }

}
