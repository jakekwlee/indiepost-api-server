package com.indiepost.dto

/**
 * Created by jake on 10/8/16.
 */
data class CategoryDto(
        var id: Long? = null,

        var parentId: Long? = null,

        var name: String? = null,

        var slug: String? = null,

        var displayOrder: Int = 0
)
