package com.indiepost.model.analytics

import com.indiepost.model.Post
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by jake on 8/9/17.
 */
@Entity
@DiscriminatorValue("Pageview")
data class Pageview(
        @NotNull
        @Column(nullable = false, columnDefinition = "bit(1) default b'0'")
        var isLandingPage: Boolean? = false,

        @Column(name = "postId", updatable = false, insertable = false)
        var postId: Long? = null
) : Stat() {
    @ManyToOne
    @JoinColumn(name = "postId")
    var post: Post? = null
}
