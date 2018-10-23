package com.indiepost.model


import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

/**
 * Created by jake on 10/29/17.
 */
@Entity
@Table(name = "CachedPostStats")
data class CachedPostStat(
        @Id
        @GeneratedValue
        @JsonIgnore
        var id: Long? = null,

        var pageviews: Long? = null,

        var uniquePageviews: Long? = null,

        var legacyPageviews: Long? = null,

        var legacyUniquePageviews: Long? = null
) {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId")
    var post: Post? = null
}