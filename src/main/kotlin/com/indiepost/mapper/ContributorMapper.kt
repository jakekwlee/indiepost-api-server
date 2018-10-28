package com.indiepost.mapper

import com.indiepost.dto.ContributorDto
import com.indiepost.enums.Types
import com.indiepost.model.Contributor
import java.time.LocalDateTime

fun Contributor.createDto(): ContributorDto {
    val dto = ContributorDto()
    dto.id = id
    dto.fullName = fullName
    dto.email = email
    dto.isEmailVisible = isEmailVisible
    dto.subEmail = subEmail
    dto.phone = phone
    dto.isPhoneVisible = isPhoneVisible
    dto.picture = picture
    dto.isPictureVisible = isPictureVisible
    dto.url = url
    dto.title = title
    dto.isTitleVisible = isTitleVisible
    dto.description = description
    dto.isDescriptionVisible = isDescriptionVisible
    contributorType?.let {
        dto.contributorType = it.toString()
    }
    displayType?.let {
        dto.displayType = it.toString()
    }
    dto.lastUpdated = lastUpdated
    dto.created = created
    dto.etc = etc
    return dto
}

fun Contributor.merge(dto: ContributorDto) {
    fullName = dto.fullName
    title = dto.title
    isTitleVisible = dto.isTitleVisible
    picture = dto.picture
    isPictureVisible = dto.isPictureVisible
    dto.email?.let {
        email = it
    }
    subEmail = dto.subEmail
    isEmailVisible = dto.isEmailVisible
    description = dto.description
    isDescriptionVisible = dto.isDescriptionVisible
    dto.contributorType?.let {
        contributorType = Types.ContributorType.valueOf(it)
    }
    dto.displayType?.let {
        displayType = Types.ContributorDisplayType.valueOf(it)
    }
    etc = dto.etc
    phone = dto.phone
    isPhoneVisible = dto.isPhoneVisible
    url = dto.url
    isUrlVisible = dto.isUrlVisible
    lastUpdated = LocalDateTime.now()
}

fun ContributorDto.createEntity(): Contributor {
    val contributor = Contributor()
    contributor.created = LocalDateTime.now()
    contributor.merge(this)
    return contributor
}

