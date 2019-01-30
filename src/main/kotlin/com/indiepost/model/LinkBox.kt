package com.indiepost.model

import com.indiepost.enums.Types
import com.indiepost.model.analytics.Link
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "LinkBoxes")
data class LinkBox(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long?,

        @Size(max = 500)
        var title: String?,

        @Size(max = 500)
        var description: String?,

        @URL
        @Size(max = 500)
        var targetUrl: String?,

        @URL
        @Size(max = 500)
        var originalUrl: String?,

        @OneToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "linkId", nullable = true)
        var link: Link?,

        @Column(name = "linkId", updatable = false, insertable = false)
        var linkId: Long?,

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "imageSetId", nullable = true)
        var imageSet: ImageSet?,

        @Column(name = "imageSetId", updatable = false, insertable = false)
        var imageSetId: Long?,

        @Enumerated(EnumType.STRING)
        var boxType: Types.LinkBoxType?,

        @Enumerated(EnumType.STRING)
        var boxFigure: Types.LinkBoxFigure?,

        @Column(nullable = false)
        var created: LocalDateTime?,

        @Column(nullable = false)
        var lastUpdated: LocalDateTime?,

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "filmId", nullable = true)
        var film: Film?,

        @Column(name = "filmId", updatable = false, insertable = false)
        var filmId: Long?,

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "bookId", nullable = true)
        var book: Book?,

        @Column(name = "bookId", updatable = false, insertable = false)
        var bookId: Long?
)