package com.indiepost.dto

data class Highlight(

        var title: String? = null,

        var excerpt: String? = null,

        var contributors: List<String>? = null,

        var tags: List<String>? = null,

        var bylineName: String? = null,

        var categoryName: String? = null,

        var creatorName: String? = null,

        var modifiedUserName: String? = null

) {

    constructor(title: String) : this() {

        this.title = title
    }

    constructor(title: String, excerpt: String) : this() {

        this.title = title
        this.excerpt = excerpt
    }
}
