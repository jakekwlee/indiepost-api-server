package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Created by jake on 8/31/17.
 */
@Entity
@Table(name = "Settings")
data class Setting(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Max(50)
        var mailHost: String? = null,

        @NotNull
        @Min(0)
        @Max(65535)
        var mailPort: Int = 0,

        @NotNull
        var mailUsername: String? = null,

        @NotNull
        @Max(50)
        var mailPassword: String? = null,

        @NotNull
        @Max(20)
        var mailPersonalName: String? = null
)
