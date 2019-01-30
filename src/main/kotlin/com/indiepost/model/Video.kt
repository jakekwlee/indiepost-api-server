package com.indiepost.model

import com.indiepost.enums.Types
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Videos")
data class Video(
        @Id
        @GeneratedValue
        var id: Long?,

        @Size(max = 100)
        var title: String?,

        @URL
        @Size(max = 500)
        var url: String?,

        @Enumerated(EnumType.STRING)
        var videoSource: Types.VideoSource,

        var playTime: Int,

        var width: Int,

        var height: Int,

        @NotNull
        @Column(nullable = false)
        var created: LocalDateTime,

        @NotNull
        @Column(nullable = false)
        var lastUpdated: LocalDateTime
)