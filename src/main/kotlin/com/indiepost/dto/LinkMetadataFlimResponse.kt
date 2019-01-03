package com.indiepost.dto

import com.indiepost.enums.Types

data class LinkMetadataFlimResponse(
        var id: Long?,
        var title: String?,
        var imageUrl: String?,
        var directors: List<String>?,
        var actors: List<String>?,
        var url: String?,
        var source: String?,
        var published: String?,
        var contentId: Long?,
        var type: String = Types.LinkBoxType.Film.toString()
)
