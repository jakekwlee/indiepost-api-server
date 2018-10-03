package com.indiepost.model

import com.indiepost.enums.Types.ImageSize
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Created by jake on 8/15/16.
 */
@Entity
@Table(name = "Images")
data class Image(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false)
        @Size(min = 2, max = 120)
        var filePath: String? = null,

        @Column(nullable = false)
        @Size(min = 2, max = 120)
        var fileName: String? = null,

        @Column(nullable = false)
        var width: Int = 0,

        @Column(nullable = false)
        var height: Int = 0,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var sizeType: ImageSize? = null,

        @Column(name = "imageSetId", updatable = false, insertable = false)
        var imageSetId: Long? = null
) : Serializable {
    companion object {
        private const val serialVersionUID = -6417761322329861824L
    }
}
