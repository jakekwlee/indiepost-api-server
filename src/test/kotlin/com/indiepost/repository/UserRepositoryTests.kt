package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import com.indiepost.enums.Types
import com.indiepost.helper.printToJson
import com.indiepost.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.transaction.Transactional


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
@Transactional
class UserRepositoryTests {
    @Inject
    private lateinit var repository: UserRepository

    @Test
    fun getTotalUsers_shouldReturnTotalUserCountProperly() {
        val totalUsers = repository.totalUsers
        assertThat(totalUsers).isNotNull().isGreaterThanOrEqualTo(50L)
        println(totalUsers)
    }

    @Test
    fun getTotalUsers_withUserRoleArgs_shouldReturnTotalUserCountProperly() {
        val totalUsers = repository.getTotalUsers(Types.UserRole.Editor)
        assertThat(totalUsers).isNotNull().isLessThan(20L)
        println(totalUsers)
    }

    @Test
    fun getTotalUsers_withDateArgs_shouldReturnTotalUserCountProperly() {
        val totalUsers = repository.getTotalUsers(LocalDateTime.now().minusDays(7), LocalDateTime.now())
        assertThat(totalUsers).isNotNull().isGreaterThanOrEqualTo(0)
        println(totalUsers)
    }

    @Test
    fun findByUserRole_shouldReturnUserListProperly() {
        val role = Types.UserRole.User
        val size = 20
        val userList = repository.findByUserRole(role, PageRequest.of(0, size))
        assertThat(userList).isNotNull.hasSize(20)
    }

    @Test
    fun search_shouldReturnUserListProperly() {
        val role = Types.UserRole.User
        val size = 20
        val userList = repository.search("Lee", role, PageRequest.of(0, size))
        assertThat(userList).isNotNull.hasAtLeastOneElementOfType(User::class.java)
        assertThat(userList.size).isLessThanOrEqualTo(size)
        printToJson(userList)
    }

    @Test
    fun searchTotal_shouldReturnSearchTotalElementsProperly() {
        val role = Types.UserRole.User
        val text = "Lee"
        val count = repository.searchTotal(text, role)
        assertThat(count).isNotNull().isGreaterThanOrEqualTo(1)

        val size = 50
        val userList = repository.search(text, role, PageRequest.of(0, size))
        assertThat(count).isEqualTo(userList.size.toLong())
    }

}
