package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Posts")
data class PostPost(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId")
        var post: Post? = null,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "relatedPostId")
        var relatedPost: Post? = null,

        @Column(name = "postId", updatable = false, insertable = false)
        var postId: Long? = null,

        @Column(name = "relatedPostId", updatable = false, insertable = false)
        var relatedPostId: Long? = null
)
