package com.indiepost.model.analytics

import com.indiepost.enums.Types
import com.indiepost.model.User
import com.indiepost.model.UserAgent
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "Visitors", indexes = [Index(columnList = "timestamp", name = "v_timestamp_idx")])
data class Visitor(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Size(max = 40)
        var appName: String? = null,

        @Size(max = 20)
        var appVersion: String? = null,

        @Size(max = 40)
        var browser: String? = null,

        @Size(max = 20)
        var browserVersion: String? = null,

        @Size(max = 40)
        var os: String? = null,

        @Size(max = 20)
        var osVersion: String? = null,

        @Size(max = 40)
        var device: String? = null,

        @Size(max = 40)
        var ipAddress: String? = null,

        @Size(max = 500)
        var referrer: String? = null,

        var isAdVisitor: Boolean = false,

        @NotNull
        @Enumerated(EnumType.STRING)
        var channel: Types.Channel = Types.Channel.NONE,

        @NotNull
        var timestamp: LocalDateTime? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId")
        var user: User? = null,

        @Column(name = "userId", insertable = false, updatable = false)
        var userId: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinColumn(name = "userAgentId")
        var userAgent: UserAgent? = null
)
