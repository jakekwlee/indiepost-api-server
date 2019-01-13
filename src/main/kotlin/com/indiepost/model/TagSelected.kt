package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "Tags_Selected")
data class TagSelected(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @OneToOne
        @JoinColumn(name = "tagId")
        var tag: Tag,

        @Column(name = "tagId", insertable = false, updatable = false)
        var tagId: Long? = null,

        @NotNull
        @Column(nullable = false)
        var priority: Int = 0
)
