package com.indiepost.model

import javax.persistence.*

@Entity
@Table(name = "Dictionary")
data class Word(
        @Id
        @GeneratedValue
        var id: Int? = null,

        @Column(nullable = false, unique = true)
        var surface: String? = null,
        var cost: Int? = null
) {
    constructor(surface: String) : this() {
        this.surface = surface
    }

    constructor(surface: String, cost: Int?) : this() {
        this.surface = surface
        this.cost = cost
    }
}
