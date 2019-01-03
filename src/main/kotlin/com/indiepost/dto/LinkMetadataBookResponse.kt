package com.indiepost.dto

import com.indiepost.enums.Types

data class LinkMetadataBookResponse(
        var id: Long?,
        var title: String?,
        var imageUrl: String?,
        var authors: List<String>?,
        var publisher: String?,
        var description: String?,
        var url: String?,
        var source: String?,
        var published: String?,
        var contentId: Long?,
        var type: String = Types.LinkBoxType.Book.toString()
)
