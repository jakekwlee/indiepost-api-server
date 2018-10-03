package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Posts_Contributors")
data class PostContributor(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "postId")
        var post: Post? = null,

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "contributorId")
        var contributor: Contributor? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
) {
    constructor(post: Post, contributor: Contributor) : this() {
        this.post = post
        this.contributor = contributor
    }

    constructor(post: Post, contributor: Contributor, priority: Int) : this() {
        this.post = post
        this.contributor = contributor
        this.priority = priority
    }
}
