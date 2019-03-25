package com.indiepost.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PTag")
data class PTag(
        @Id
        @GeneratedValue
        var id: Int? = null,

        var postId: Long? = null,

        var primaryTag: String? = null
)
