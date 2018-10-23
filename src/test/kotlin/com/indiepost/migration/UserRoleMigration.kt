package com.indiepost.migration

import com.indiepost.NewIndiepostApplication
import com.indiepost.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
@Transactional
class UserRoleMigration {
    @Inject
    private lateinit var repository: UserRepository

    @Test
    //    @Rollback(false)
    fun migrateUserRoles() {
        val userList = repository.findAll(PageRequest.of(0, Integer.MAX_VALUE))
        // warning! this code produces n + 1 select problem
        for (user in userList) {
            val role = user.highestRole
            if (user.roleType == null || user.roleType != role) {
                user.roleType = role
            }
        }
    }
}
