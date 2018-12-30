package com.indiepost.dto

import com.indiepost.enums.Types

data class LinkMetadataResponse(
        var id: Long?,
        var title: String?,
        var imageUrl: String?,
        var directors: List<String>?,
        var actors: List<String>?,
        var url: String?,
        var source: String?,
        var published: String?,
        var type: String = Types.LinkBoxType.Default.toString(),
        var contentId: Long?
)
