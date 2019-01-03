package com.indiepost.dto

import com.indiepost.enums.Types

data class LinkMetadataResponse(
        var id: Long?,
        var title: String?,
        var imageUrl: String?,
        var url: String?,
        var source: String?,
        var description: String?,
        val type: String = Types.LinkBoxType.Default.toString()
)
