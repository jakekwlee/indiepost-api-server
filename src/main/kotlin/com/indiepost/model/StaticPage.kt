package com.indiepost.model

import com.indiepost.enums.Types
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by jake on 17. 3. 5.
 */
@Entity
@Table(name = "Pages")
data class StaticPage(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false)
        var title: String? = null,

        @Column(nullable = false, columnDefinition = "LONGTEXT")
        var content: String? = null,

        @Column(unique = true, nullable = false)
        var slug: String? = null,

        @Column(nullable = false)
        var createdAt: LocalDateTime? = null,

        var modifiedAt: LocalDateTime? = null,

        @Column(nullable = false)
        var displayOrder: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "authorId", nullable = false)
        var author: User? = null,

        @Column(name = "authorId", nullable = false, insertable = false, updatable = false)
        var authorId: Long? = null,

        var type: String = "",

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var status: Types.PostStatus = Types.PostStatus.PUBLISH

)
