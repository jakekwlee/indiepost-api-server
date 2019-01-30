package com.indiepost.model

import org.hibernate.validator.constraints.URL
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Books")
data class Book(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long?,

        @Size(max = 100)
        var title: String?,

        @URL
        @Size(max = 500)
        var sourceUrl: String?,

        @URL
        @Size(max = 500)
        var imageUrl: String?,

        @Size(max = 100)
        var authors: String?,

        @Size(max = 100)
        var publisher: String?,

        var pulished: LocalDate?,

        @Size(max = 20)
        var isbn: String,

        @NotNull
        @Column(nullable = false)
        var created: LocalDateTime?,

        @NotNull
        @Column(nullable = false)
        var lastUpdated: LocalDateTime?
)