package com.indiepost.mapper

import com.indiepost.dto.ProfileDto
import com.indiepost.enums.Types
import com.indiepost.model.Profile
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import java.time.LocalDateTime

fun Profile.createDto(): ProfileDto {
    val dto = ProfileDto()
    dto.id = id
    dto.description = description
    dto.showDescription = showDescription
    dto.displayName = displayName
    dto.etc = etc
    dto.label = label
    dto.showLabel = showLabel
    dto.profileType = profileType.toString()
    dto.email = email
    dto.subEmail = subEmail
    dto.showEmail = showEmail
    dto.slug = slug
    created?.let {
        dto.created = localDateTimeToInstant(it)
    }
    lastUpdated?.let {
        dto.lastUpdated = localDateTimeToInstant(it)
    }
    return dto
}

fun Profile.createDtoWithPrivacy(): ProfileDto {
    val dto = ProfileDto()
    dto.id = id
    dto.description = description
    dto.showDescription = showDescription
    dto.displayName = displayName
    dto.etc = etc
    dto.fullName = fullName
    dto.label = label
    dto.showLabel = showLabel
    dto.profileState = profileState.toString()
    dto.profileType = profileType.toString()
    dto.email = email
    dto.subEmail = subEmail
    dto.showEmail = showEmail
    dto.slug = slug
    created?.let {
        dto.created = localDateTimeToInstant(it)
    }
    lastUpdated?.let {
        dto.lastUpdated = localDateTimeToInstant(it)
    }
    return dto
}

fun ProfileDto.createEntity(): Profile {
    val profile = Profile(slug = this.slug!!)
    profile.created = LocalDateTime.now()
    profile.lastUpdated = LocalDateTime.now()
    profile.description = description
    profile.email = email
    profile.subEmail = subEmail
    profile.label = label
    profile.phone = phone
    profile.picture = picture
    profile.etc = etc
    profile.showDescription = showDescription
    profile.showEmail = showEmail
    profile.showLabel = showLabel
    profile.showPicture = false
    profileState?.let {
        profile.profileState = Types.ProfileState.valueOf(it)
    }
    profileType?.let {
        profile.profileType = Types.ProfileType.valueOf(it)
    }
    displayName?.let {
        profile.displayName = it
    }
    fullName?.let {
        profile.fullName = it
    }
    return profile
}

fun Profile.merge(dto: ProfileDto) {
    dto.slug?.let {
        slug = it
    }
    dto.fullName?.let {
        fullName = it
    }
    dto.displayName?.let {
        displayName = it
    }
    dto.profileState?.let {
        profileState = Types.ProfileState.valueOf(it)
    }
    dto.profileType?.let {
        profileType = Types.ProfileType.valueOf(it)
    }
    description = dto.description
    showDescription = dto.showDescription
    showLabel = dto.showLabel
    picture = dto.picture
    showPicture = dto.showPicture
    etc = dto.etc
    email = dto.email
    subEmail = dto.subEmail
    showEmail = dto.showEmail
    label = dto.label
    phone = dto.phone
    lastUpdated = LocalDateTime.now()
}

