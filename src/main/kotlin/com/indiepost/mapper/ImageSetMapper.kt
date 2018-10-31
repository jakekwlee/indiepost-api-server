package com.indiepost.mapper

import com.indiepost.dto.ImageDto
import com.indiepost.dto.ImageSetDto
import com.indiepost.model.Image
import com.indiepost.model.ImageSet
import com.indiepost.utils.DateUtil.localDateTimeToInstant

fun ImageSet.createDto(): ImageSetDto {
    val imageSetDto = ImageSetDto()
    imageSetDto.id = id
    imageSetDto.contentType = contentType
    this.uploadedAt?.let {
        imageSetDto.uploadedAt = localDateTimeToInstant(it)
    }
    original?.let {
        imageSetDto.original = it.createDto()
    }
    large?.let {
        imageSetDto.large = it.createDto()
    }
    optimized?.let {
        imageSetDto.optimized = it.createDto()
    }
    small?.let {
        imageSetDto.small = it.createDto()
    }
    thumbnail?.let {
        imageSetDto.thumbnail = it.createDto()
    }
    return imageSetDto
}

fun Image.createDto(): ImageDto {
    return ImageDto(id, filePath, width, height)
}