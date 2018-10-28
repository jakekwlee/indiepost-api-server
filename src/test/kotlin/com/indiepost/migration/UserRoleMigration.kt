package com.indiepost.migration

import com.indiepost.repository.UserRepository
import org.springframework.data.domain.PageRequest
import javax.inject.Inject

//@ExtendWith(SpringExtension::class)
//@SpringBootTest(classes = arrayOf(com.indiepost.NewIndiepostApplication::class))
//@WebAppConfiguration
//@Transactional
class UserRoleMigration {
    @Inject
    private lateinit var repository: UserRepository

    //    @Test
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
