package com.indiepost.dto.analytics

data class RawDataReportRow(
        var campaignName: String? = null,

        var linkName: String? = null,

        var linkTo: String? = null,

        var device: String? = null,

        var os: String? = null,

        var osVersion: String? = null,

        var browser: String? = null,

        var browserVersion: String? = null,

        var ipAddress: String? = null,

        var timestamp: String? = null
)
