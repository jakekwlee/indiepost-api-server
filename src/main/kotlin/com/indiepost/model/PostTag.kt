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
        var priority: Int = 0,

        @Column(name = "postId", insertable = false, updatable = false)
        var postId: Long? = null,

        @Column(name = "tagId", insertable = false, updatable = false)
        var tagId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId")
        var post: Post? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tagId")
        var tag: Tag? = null
)

