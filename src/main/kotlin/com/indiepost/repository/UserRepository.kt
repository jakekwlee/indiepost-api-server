package com.indiepost.repository

import com.indiepost.enums.Types
import com.indiepost.enums.Types.UserGender
import com.indiepost.enums.Types.UserState
import com.indiepost.model.User
import org.springframework.data.domain.Pageable

import java.time.LocalDateTime

/**
 * Created by jake on 7/26/16.
 */
interface UserRepository {

    val totalUsers: Long

    fun save(user: User)

    fun delete(user: User)

    fun findById(id: Long?): User?

    fun findByUsername(username: String): User?

    fun search(text: String, role: Types.UserRole, pageable: Pageable): List<User>

    fun findCurrentUser(): User?

    fun findByEmail(email: String): User?

    fun findAll(pageable: Pageable): List<User>

    fun findByState(state: UserState, pageable: Pageable): List<User>

    fun findByGender(gender: UserGender, pageable: Pageable): List<User>

    fun findByUserRole(role: Types.UserRole, pageable: Pageable): List<User>

    fun getTotalUsers(role: Types.UserRole): Long?

    fun getTotalUsers(from: LocalDateTime, to: LocalDateTime): Long?

    fun searchTotal(text: String, role: Types.UserRole): Long?
}