package com.indiepost.model

import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "Categories")
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false, unique = true)
        @Size(min = 3, max = 20)
        var name: String? = null,

        @Column(nullable = false, unique = true)
        @Size(min = 3, max = 20)
        var slug: String? = null,

        @Column(nullable = false)
        var displayOrder: Int = 0,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parentId")
        var parent: Category? = null,

        @Column(name = "parentId", insertable = false, updatable = false)
        var parentId: Long? = null,

        @OneToMany(mappedBy = "parent", orphanRemoval = true, fetch = FetchType.LAZY)
        var categories: MutableList<Category>? = null,

        @OneToMany(mappedBy = "category")
        var posts: MutableList<Post>? = null
)
