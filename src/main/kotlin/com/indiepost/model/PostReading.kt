package com.indiepost.model

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "Posts_Users_Readings")
data class PostReading(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(name = "userId", insertable = false, updatable = false)
        var userId: Long? = null,

        @Column(name = "postId", insertable = false, updatable = false)
        var postId: Long? = null,

        @Column(nullable = false)
        var created: LocalDateTime? = null,

        @Column(nullable = false)
        var lastRead: LocalDateTime? = null,

        @Column(nullable = false)
        var readCount: Int = 1,

        var isVisible: Boolean = true,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        var user: User? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId", nullable = false)
        var post: Post? = null
) {
    fun increaseReadCount() {
        this.readCount = readCount + 1
    }
}