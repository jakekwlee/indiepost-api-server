package com.indiepost.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.indiepost.enums.Types.ImageSize
import org.hibernate.annotations.*
import org.hibernate.annotations.CascadeType
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Size

/**
 * Created by jake on 9/7/16.
 */
@Entity
@Table(name = "ImageSets")
data class ImageSet(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Fetch(FetchMode.JOIN)
        @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
        @Cascade(CascadeType.ALL, CascadeType.SAVE_UPDATE)
        @JoinColumn(name = "imageSetId")
        @BatchSize(size = 20)
        @JsonIgnore
        var images: Set<Image>? = null,

        @Column(nullable = false)
        @Size(min = 9, max = 10)
        var contentType: String? = null,

        @Size(max = 300)
        var caption: String? = null,

        @Size(max = 20)
        @Column(unique = true, nullable = false)
        var prefix: String? = null,

        @Column(nullable = false)
        var uploadedAt: LocalDateTime? = null
) : Serializable {
    val original: Image?
        get() = findByImageSize(ImageSize.ORIGINAL)

    val large: Image?
        get() = findByImageSize(ImageSize.LARGE)

    val optimized: Image?
        get() = findByImageSize(ImageSize.OPTIMIZED)

    val small: Image?
        get() = findByImageSize(ImageSize.SMALL)

    val thumbnail: Image?
        get() = findByImageSize(ImageSize.THUMBNAIL)

    private fun findByImageSize(sizeType: ImageSize): Image? {
        if (images == null) {
            return null
        }
        for (image in images!!) {
            if (image.sizeType == sizeType) {
                return image
            }
        }
        return null
    }

    companion object {
        private const val serialVersionUID = -6176638635434014551L
    }
}
