package com.indiepost.dto

data class CreateResponse(
        var id: Long? = null,

        var originalId: Long? = null

) {
    constructor(id: Long) : this() {
        this.id = id
    }
}
