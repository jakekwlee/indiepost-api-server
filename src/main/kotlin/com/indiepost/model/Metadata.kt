package com.indiepost.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by jake on 10/29/17.
 */
@Entity
@Table(name = "Metadata")
data class Metadata(
        @Id
        @GeneratedValue
        var id: Long? = null,

        var postStatsLastUpdated: LocalDateTime? = null,

        var searchIndexLastUpdated: LocalDateTime? = null
)
