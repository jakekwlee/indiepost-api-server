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

        private var profiles: MutableList<String> = ArrayList(),

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

    fun getProfiles(): MutableList<String> {
        return profiles
    }

    fun setProfiles(profiles: MutableList<String>) {
        this.profiles = profiles
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

    fun addProfile(profile: String) {
        profiles.add(profile)
    }

    fun removeProfile(profile: String) {
        profiles.remove(profile)
    }

    companion object {
        private const val serialVersionUID = 6587432935142567307L
    }
}
