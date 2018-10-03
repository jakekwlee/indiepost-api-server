package com.indiepost.model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Tags")
data class PostTag(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId")
        var post: Post? = null,


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "tagId")
        var tag: Tag? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
) : Serializable {
    constructor(post: Post, tag: Tag, @NotNull priority: Int) : this() {
        this.post = post
        this.tag = tag
        this.priority = priority
    }

    companion object {
        private const val serialVersionUID = 5610937158697175938L
    }
}
