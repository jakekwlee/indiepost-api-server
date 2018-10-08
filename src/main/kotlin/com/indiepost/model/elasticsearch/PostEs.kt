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

        private var contributors: MutableList<String> = ArrayList(),

        private var tags: MutableList<String> = ArrayList(),

        var status: String? = null,

        var creatorId: Long? = null,

        var modifiedUserId: Long? = null,

        var creatorName: String? = null,

        var modifiedUserName: String? = null,

        var categoryName: String? = null

) : Serializable {
    constructor(id: Long?) : this() {
        this.id = id
    }

    fun getContributors(): MutableList<String> {
        return contributors
    }

    fun setContributors(contributors: MutableList<String>) {
        this.contributors = contributors
    }

    fun getTags(): MutableList<String> {
        return tags
    }

    fun setTags(tags: MutableList<String>) {
        this.tags = tags
    }

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun removeTag(tag: String) {
        tags.remove(tag)
    }

    fun addContributor(contributor: String) {
        contributors.add(contributor)
    }

    fun removeContributor(contributor: String) {
        contributors.remove(contributor)
    }

    companion object {
        private const val serialVersionUID = 6587432935142567307L
    }
}
