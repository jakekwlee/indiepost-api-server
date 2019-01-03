package com.indiepost.dto

data class LinkMetadataRequest(
        var text: String?,
        var targetUrl: String?,
        var type: String?,
        var size: Int
)
