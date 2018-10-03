package com.indiepost.model

import java.io.Serializable
import java.util.*
import java.util.stream.Collectors
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Created by jake on 7/25/16.
 */

@Entity
@Table(name = "Tags")
data class Tag(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Size(min = 1, max = 50)
        @Column(nullable = false, unique = true)
        var name: String? = null,

        @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
        var postTags: MutableList<PostTag> = ArrayList()
) : Serializable {

    val posts: MutableList<Post>
        get() = postTags.stream()
                .map { postTag -> postTag.post }
                .collect(Collectors.toList())

    constructor(name: String) : this() {
        this.name = name
    }

    companion object {
        private const val serialVersionUID = 4776718128460861941L
    }
}

