package com.indiepost.model

import com.indiepost.enums.Types
import javax.persistence.*

@Entity
@Table(name = "ContentBlocks")
data class ContentBlock(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long?,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId", nullable = false)
        var post: Post?,

        @Column(name = "postId", updatable = false, insertable = false)
        var postId: Long?,

        var blockType: Types.PostBlockType,

        var priority: Int = 0,

        @Column(columnDefinition = "TEXT")
        var html: String? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "imageSetId", nullable = true)
        var imageSet: ImageSet? = null,

        @Column(name = "imageSetId", updatable = false, insertable = false)
        var imageSetId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "videoId", nullable = true)
        var video: Video? = null,

        @Column(name = "videoId", updatable = false, insertable = false)
        var videoId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "linkBoxId", nullable = true)
        var linkBox: LinkBox? = null,

        @Column(name = "linkBoxId", updatable = false, insertable = false)
        var linkBoxId: Long? = null
)