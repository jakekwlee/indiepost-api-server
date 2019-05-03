package com.indiepost.mapper

import com.indiepost.dto.TagDto
import com.indiepost.model.Tag

fun Tag.toDto(): TagDto {
    return TagDto(id = id, text = name)
}