package com.indiepost.model

import org.hibernate.validator.constraints.URL
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Films")
data class Film(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long?,

        @Size(max = 100)
        var directors: String?,

        @Size(max = 100)
        var actors: String?,

        var releaseDate: LocalDate?,

        @URL
        @Size(max = 500)
        var imageUrl: String?,

        @URL
        @Size(max = 500)
        var sourceUrl: String?,

        @Size(max = 50)
        var genre: String?,

        var playTime: Int,

        @NotNull
        @Column(nullable = false)
        var created: LocalDateTime?,

        @NotNull
        @Column(nullable = false)
        var lastUpdated: LocalDateTime?
)