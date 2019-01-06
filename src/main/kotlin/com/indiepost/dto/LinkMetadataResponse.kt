package com.indiepost.dto

data class LinkMetadataResponse(
        var id: Long?,
        var title: String?,
        var imageUrl: String?,
        var url: String?,
        var source: String?,
        var description: String? = null,
        var publisher: String? = null,
        var directors: List<String>? = null,
        var actors: List<String>? = null,
        var authors: List<String>? = null,
        var published: String? = null,
        var productId: Long? = null,
        val type: String
)
