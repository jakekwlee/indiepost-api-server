package com.indiepost.mapper

import com.indiepost.dto.ImageSetDto
import com.indiepost.model.ImageSet
import com.indiepost.utils.DateUtil.localDateTimeToInstant

fun ImageSet.createDto(): ImageSetDto {
    val imageSetDto = ImageSetDto()
    imageSetDto.id = this.id
    imageSetDto.contentType = this.contentType
    this.uploadedAt?.let {
        imageSetDto.uploadedAt = localDateTimeToInstant(it)
    }
    val original = this.original
    if (original != null) {
        imageSetDto.original = original.filePath
        imageSetDto.width = original.width
        imageSetDto.height = original.height
    } else {
        imageSetDto.width = 700
        imageSetDto.height = 400
    }
    this.large?.let {
        imageSetDto.large = it.filePath
    }
    this.optimized?.let {
        imageSetDto.optimized = it.filePath
    }
    this.small?.let {
        imageSetDto.small = it.filePath
    }
    this.thumbnail?.let {
        imageSetDto.thumbnail = it.filePath
    }
    return imageSetDto
}
