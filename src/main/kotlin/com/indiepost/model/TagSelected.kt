package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Tags_Selected")
data class TagSelected(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tagId")
        var tag: Tag,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
)
