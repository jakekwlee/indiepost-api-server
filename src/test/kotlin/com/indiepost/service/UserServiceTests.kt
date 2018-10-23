package com.indiepost.service

import com.indiepost.NewIndiepostApplication
import com.indiepost.dto.user.UserDto
import com.indiepost.dto.user.UserProfileDto
import com.indiepost.utils.DateUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * Created by jake on 17. 11. 13.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class UserServiceTests {

    @Inject
    private lateinit var userService: UserService

    @Test
    fun usersShouldHaveUniqueId() {
        val users = userService.findAllUsers(0, 100, true)
        var id: Long? = -1L
        for ((id1) in users) {
            assertThat(id1).isNotEqualTo(id)
            id = id1
        }
    }

    //    @Test
    @WithMockUser("auth0|5a88547af5c8213cb27caf41")
    fun createOrUpdate_ShouldUpdateUserProperlyWhenUserAlreadyExists() {
        val dto = UserDto()

        dto.username = "auth0|5a88547af5c8213cb27caf41"

        dto.displayName = "Test Name"
        dto.gender = "MALE"
        dto.picture = "https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"

        val updatedAt = DateUtil.localDateTimeToInstant(LocalDateTime.of(2018, 2, 26, 12, 0))
        dto.updatedAt = updatedAt
        dto.gender = "FEMALE"
        dto.roles = Arrays.asList("Administrator", "Editor", "User")

        val (isNewUser, resultDto) = userService.syncAuthorization(dto)

        assertThat(isNewUser).isTrue()
        assertThat(resultDto).isNotNull()
        assertThat(resultDto.id).isNotNull()
        assertThat(resultDto.email).isEqualTo(dto.email)
        assertThat(resultDto.displayName).isEqualTo(dto.displayName)
        assertThat(resultDto.picture).isEqualTo(dto.picture)
        assertThat(resultDto.updatedAt).isEqualTo(dto.updatedAt)
        assertThat(resultDto.gender).isEqualTo(dto.gender)
        assertThat(resultDto.username).isEqualTo(dto.username)
        assertThat(resultDto.roleType).isEqualTo(dto.roleType)
        assertThat(resultDto.updatedAt).isEqualTo(dto.updatedAt)

        assertThat(resultDto.roles).containsOnlyElementsOf(dto.roles)
    }

    @Test
    @WithMockUser(username = "auth0|5b213cd8064de34cde981b47")
    fun update_shouldWorkProperly() {
        val dto = UserProfileDto()
        dto.id = 1L
        dto.username = "auth0|5b213cd8064de34cde981b47"
        dto.email = "bwv1050@gmail.com"
        dto.displayName = "바보"
        userService!!.update(dto)
    }
}
