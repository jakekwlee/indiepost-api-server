package com.indiepost.repository

import com.indiepost.enums.Types.UserRole
import com.indiepost.model.Role

/**
 * Created by jake on 7/27/16.
 */
interface RoleRepository {

    fun save(role: UserRole)

    fun update(role: UserRole)

    fun delete(role: UserRole)

    fun findById(id: Long?): Role?

    fun findByUserRole(role: UserRole): Role?

    fun findByUserRoleString(role: String): Role?
}