package com.indiepost.mapper

import com.indiepost.dto.StaticPageDto
import com.indiepost.model.StaticPage
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import java.time.Instant

fun StaticPage.createDto(): StaticPageDto {
    val staticPageDto = StaticPageDto()
    staticPageDto.id = id
    staticPageDto.title = title
    staticPageDto.content = content
    staticPageDto.type = type
    staticPageDto.authorDisplayName = author!!.displayName
    staticPageDto.slug = slug
    staticPageDto.displayOrder = displayOrder
    modifiedAt?.let {
        staticPageDto.modifiedAt = localDateTimeToInstant(it)
    }
    createdAt?.let {
        staticPageDto.createdAt = localDateTimeToInstant(it)
    }
    staticPageDto.status = status
    staticPageDto.lastRequested = Instant.now()
    return staticPageDto
}
