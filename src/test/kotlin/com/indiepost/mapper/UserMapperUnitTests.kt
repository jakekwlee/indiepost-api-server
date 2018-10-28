package com.indiepost.mapper

import com.indiepost.dto.user.UserDto
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class UserMapperUnitTests {

    @Test
    fun userDtoToUser_shouldWorkProperly() {
        val dto = UserDto()

        dto.username = "auth0|5a88547af5c8213cb27caf41"

        dto.displayName = "Test Name"
        dto.email = "sysadmin@indiepost.co.kr"
        dto.gender = "MALE"
        dto.picture = "https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
        val updated = localDateTimeToInstant(LocalDateTime.of(2018, 2, 10, 12, 0))
        dto.updatedAt = updated
        dto.roles = Arrays.asList("Administrator", "Editor", "User")
        dto.roleType = "Administrator"
        dto.gender = "MALE"
        val user = dto.createEntity()

        assertThat(user).isNotNull()
        assertThat(user.email).isEqualTo(dto.email)
        assertThat(user.displayName).isEqualTo(dto.displayName)
        assertThat(user.picture).isEqualTo(dto.picture)
        assertThat(localDateTimeToInstant(user.updatedAt!!).isAfter(dto.updatedAt)).isTrue()
        assertThat(user.joinedAt).isNotNull()
        assertThat(user.gender!!.toString()).isEqualTo(dto.gender)
        assertThat(user.username).isEqualTo(dto.username)

        assertThat(user.roles.size).isZero()
    }

}
