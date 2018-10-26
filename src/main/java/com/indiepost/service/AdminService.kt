package com.indiepost.service

import com.indiepost.dto.AdminInitialResponse
import com.indiepost.dto.user.UserDto
import com.indiepost.enums.Types.UserRole

/**
 * Created by jake on 10/8/16.
 */
interface AdminService {

    fun getCurrentUserDto(): UserDto

    fun getUserDtoList(role: UserRole): List<UserDto>

    fun getUserDtoList(page: Int, maxResults: Int, isDesc: Boolean): List<UserDto>

    fun buildInitialResponse(): AdminInitialResponse
}
