package com.indiepost.model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Posts")
data class PostPost(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId")
        var post: Post? = null,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "relatedPostId")
        var relatedPost: Post? = null,

        @Column(name = "postId", updatable = false, insertable = false)
        var postId: Long? = null,

        @Column(name = "relatedPostId", updatable = false, insertable = false)
        var relatedPostId: Long? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
) : Serializable {
    companion object {
        private val serialVersionUID = 2610937958697175939L
    }

    constructor(post: Post, relatedPost: Post, @NotNull priority: Int) : this() {
        this.post = post
        this.relatedPost = relatedPost
        this.priority = priority
    }
}
