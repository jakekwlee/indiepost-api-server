package com.indiepost.service

import com.indiepost.enums.Types.UserRole
import com.indiepost.model.Role

/**
 * Created by jake on 8/4/16.
 */
interface RoleService {

    fun save(role: UserRole)

    fun update(role: UserRole)

    fun delete(role: UserRole)

    fun findById(id: Long): Role?

    fun findByName(name: String): Role?

    fun findAll(): List<Role>
}
