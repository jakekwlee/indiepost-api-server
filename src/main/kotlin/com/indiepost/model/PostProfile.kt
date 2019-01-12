package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Profiles")
data class PostProfile(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "postId")
        var post: Post? = null,

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "profileId")
        var profile: Profile? = null
)
