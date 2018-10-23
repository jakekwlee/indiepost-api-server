package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Tags")
data class PostTag(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
) {
    constructor(post: Post, tag: Tag, @NotNull priority: Int) : this() {
        this.post = post
        this.tag = tag
        this.priority = priority
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    var post: Post? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagId")
    var tag: Tag? = null
}
