package com.indiepost.service

import com.indiepost.dto.AdminInitialResponse
import com.indiepost.dto.TagDto
import com.indiepost.dto.user.UserDto
import com.indiepost.enums.Types.UserRole
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.mapper.createDto
import com.indiepost.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
class AdminServiceImpl @Inject constructor(
        private val adminPostService: AdminPostService,
        private val userService: UserService,
        private val tagService: TagService,
        private val profileService: ProfileService) : AdminService {

    override fun buildInitialResponse(): AdminInitialResponse {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        val adminInitialResponse = AdminInitialResponse()
        adminInitialResponse.currentUser = currentUser.createDto()
        adminInitialResponse.authorNames = adminPostService.findAllBylineNames()
        adminInitialResponse.selectedTags = tagService.findSelected()
        // TODO for test
        adminInitialResponse.profiles = profileService.getSummaryDtoList()
        adminInitialResponse.postTitles = adminPostService.getAllTitles()
        val tagList = tagService.find()
        val tags = tagList.stream()
                .map { (id, name) -> TagDto(id, name!!.toLowerCase()) }
                .collect(Collectors.toList())
        adminInitialResponse.tags = tags
        return adminInitialResponse
    }

    override fun getUserDtoList(role: UserRole): List<UserDto> {
        val authors = userService.findByRole(role, 1, 1000000, true)
        return userListToUserDtoList(authors)
    }

    override fun getUserDtoList(page: Int, maxResults: Int, isDesc: Boolean): List<UserDto> {
        val userList = userService.findAllUsers(page, maxResults, isDesc)
        return userListToUserDtoList(userList)
    }

    override fun getCurrentUserDto(): UserDto {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        return currentUser.createDto()
    }

    private fun userListToUserDtoList(userList: List<User>): List<UserDto> {
        val userDtoList = ArrayList<UserDto>()
        for (user in userList) {
            userDtoList.add(user.createDto())
        }
        return userDtoList
    }
}
