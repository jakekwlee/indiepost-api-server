package com.indiepost.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Bookmarks")
data class Bookmark(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false)
        var created: LocalDateTime? = null,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId", nullable = false)
        var post: Post? = null,

        @Column(name = "postId", insertable = false, updatable = false)
        var postId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        var user: User? = null,

        @Column(name = "userId", insertable = false, updatable = false)
        var userId: Long? = null
)
