package com.indiepost.model

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "PostReadings")
data class PostReading(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        var user: User? = null,

        @Column(name = "userId", insertable = false, updatable = false)
        var userId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId", nullable = false)
        var post: Post? = null,

        @Column(name = "postId", insertable = false, updatable = false)
        var postId: Long? = null,

        @Column(nullable = false)
        var created: LocalDateTime? = null,

        @Column(nullable = false)
        var lastRead: LocalDateTime? = null,

        @Column(nullable = false)
        var readCount: Int = 1,

        var isVisible: Boolean = true
) : Serializable {
    fun increaseReadCount() {
        this.readCount = readCount + 1
    }

    companion object {
        private const val serialVersionUID = 5793341286387473902L
    }
}