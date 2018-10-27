package com.indiepost.service

import com.indiepost.dto.user.SyncAuthorizationResponse
import com.indiepost.dto.user.UserDto
import com.indiepost.dto.user.UserProfileDto
import com.indiepost.enums.Types.UserRole
import com.indiepost.model.User

/**
 * Created by jake on 7/27/16.
 */
interface UserService {

    val currentUserDto: UserDto?

    fun findCurrentUser(): User?

    fun update(dto: UserProfileDto)

    fun syncAuthorization(dto: UserDto): SyncAuthorizationResponse

    fun findByRole(role: UserRole, page: Int, maxResults: Int, isDesc: Boolean): List<User>

    fun findAllUsers(page: Int, maxResults: Int, isDesc: Boolean): List<User>
}
