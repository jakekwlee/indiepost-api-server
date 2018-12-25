package com.indiepost.dto

import com.indiepost.enums.Types

data class LinkBoxResponse(
        var title: String?,
        var imageUrl: String?,
        var data: List<String>?,
        var url: String?,
        var source: String?,
        var type: String = Types.LinkBoxType.Default.toString()
)
