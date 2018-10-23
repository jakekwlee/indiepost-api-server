package com.indiepost.model.analytics

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by jake on 8/7/17.
 */
@Entity
@Table(name = "Campaigns")
data class Campaign(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @NotNull
        var clientName: String? = null,

        @NotNull
        var name: String? = null,

        @NotNull
        var startAt: LocalDateTime? = null,

        @NotNull
        var endAt: LocalDateTime? = null,

        @NotNull
        var createdAt: LocalDateTime? = null,

        var goal: Long? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign", cascade = [CascadeType.ALL])
    var links: MutableList<Link> = ArrayList()

}
