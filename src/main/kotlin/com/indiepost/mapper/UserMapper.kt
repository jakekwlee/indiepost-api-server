package com.indiepost.mapper

import com.indiepost.dto.user.UserDto
import com.indiepost.enums.Types
import com.indiepost.model.User
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import java.time.LocalDateTime
import java.util.stream.Collectors


fun User.merge(dto: UserDto) {
    this.username = dto.username
    this.displayName = dto.displayName
    this.email = dto.email
    this.picture = dto.picture
    val gender = dto.gender ?: "UNIDENTIFIED"
    this.gender = Types.UserGender.valueOf(gender)
    this.updatedAt = LocalDateTime.now()
}

fun UserDto.createEntity(): User {
    val user = User()
    user.username = this.username
    user.displayName = this.displayName
    user.email = this.email
    user.picture = this.picture
    user.joinedAt = LocalDateTime.now()
    user.updatedAt = LocalDateTime.now()
    val gender = this.gender ?: "UNIDENTIFIED"
    user.gender = Types.UserGender.valueOf(gender)
    return user
}

fun User.createDto(): UserDto {
    val userDto = UserDto()
    userDto.id = this.id
    userDto.username = this.username
    userDto.displayName = this.displayName
    userDto.email = this.email
    userDto.gender = this.gender!!.toString()
    userDto.picture = this.picture
    userDto.profile = this.profile
    userDto.joinedAt = localDateTimeToInstant(this.joinedAt!!)
    userDto.updatedAt = localDateTimeToInstant(this.updatedAt!!)
    val roles = this.roles.stream()
            .map { role -> role.roleType!!.toString() }
            .collect(Collectors.toList<String>())
    userDto.roles = roles
    userDto.roleType = this.roleType!!.toString()
    return userDto
}
