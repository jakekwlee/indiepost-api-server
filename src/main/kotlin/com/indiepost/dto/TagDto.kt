package com.indiepost.dto

/**
 * Created by jake on 10/8/16.
 */
data class TagDto(

        var id: Long? = null,

        var name: String? = null
) {
    constructor(id: Long, name: String) : this() {
        this.id = id
        this.name = name
    }
}
