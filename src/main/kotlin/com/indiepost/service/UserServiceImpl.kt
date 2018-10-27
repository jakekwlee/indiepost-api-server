package com.indiepost.service

import com.indiepost.dto.user.SyncAuthorizationResponse
import com.indiepost.dto.user.UserDto
import com.indiepost.dto.user.UserProfileDto
import com.indiepost.enums.Types.UserRole
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.mapper.UserMapper.userDtoToUser
import com.indiepost.model.ManagementToken
import com.indiepost.model.User
import com.indiepost.model.toDto
import com.indiepost.repository.ManagementTokenRepository
import com.indiepost.repository.RoleRepository
import com.indiepost.repository.UserRepository
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject


/**
 * Created by jake on 7/27/16.
 */
@Service
@Transactional
class UserServiceImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val tokenRepository: ManagementTokenRepository) : UserService {

    override val currentUserDto: UserDto?
        get() {
            val user = findCurrentUser() ?: return null
            return toUserDto(user)
        }

    private fun auth0ManagementAPIAccessToken(): String? {
        val token = tokenRepository.findById(1)
        if (token.isPresent && token.get().expireAt!!.isAfter(LocalDateTime.now())) {
            return token.get().accessToken
        }

        try {
            val response = Unirest.post("https://indiepost.auth0.com/oauth/token")
                    .header("content-type", "application/json")
                    .body("{\"grant_type\":\"client_credentials\",\"client_id\": \"iJPAh5OMMR4jfFZRVYhX1TxZXFjGYDJK\",\"client_secret\": \"OpC17nCfe0LMGKG6m9deRNwK4FFflMJqX_Jrtcae2HxVwMBXMzTAfN2YDfdcTA5I\",\"audience\": \"https://indiepost.auth0.com/api/v2/\",\"scope\": \"update:users\"}")
                    .asJson()
            val jsonObject = response.body.getObject()
            val accessToken = jsonObject.get("access_token") as String
            val expiresIn = jsonObject.get("expires_in") as Int
            val expireAt = LocalDateTime.now().plusSeconds(expiresIn.toLong())
            val managementToken: ManagementToken

            if (token.isPresent) {
                managementToken = token.get()
                managementToken.accessToken = accessToken
                managementToken.expireAt = expireAt
            } else {
                managementToken = ManagementToken(accessToken, expireAt)
            }

            tokenRepository.save(managementToken)

            return accessToken
        } catch (e: UnirestException) {
            e.printStackTrace()
            throw UnauthorizedException()
        }
    }

    override fun update(dto: UserProfileDto) {
        val user = findCurrentUser()
        if (user == null ||
                user.id != dto.id ||
                user.username != dto.username) {
            throw UnauthorizedException()
        }
        if (dto.displayName != null) {
            user.displayName = dto.displayName
        }
    }

    override fun syncAuthorization(dto: UserDto): SyncAuthorizationResponse {
        var user: User? = findCurrentUser()
        val now = LocalDateTime.now()

        // if user is newly joined
        if (user == null) {
            user = userDtoToUser(dto)
            if (StringUtils.isEmpty(user!!.displayName)) {
                user.displayName = "NO NAME"
            }
            user.joinedAt = now
            user.lastLogin = now
            dto.roles?.let {
                addRolesToUser(user, it)
            }
            userRepository.save(user)
            return SyncAuthorizationResponse(true, user.toDto())
        }

        if (user.username != dto.username) {
            throw UnauthorizedException()
        }

        user.lastLogin = now
        val originalRoles = user.roles
                .stream()
                .map { (_, roleType) -> roleType!!.toString() }
                .collect(Collectors.toList())

        // if user roles have changed
        if (!equalLists(originalRoles, dto.roles)) {
            dto.roles?.let {
                addRolesToUser(user, it)
            }
            user.updatedAt = now
            userRepository.save(user)
        }
        return SyncAuthorizationResponse(false, user.toDto())
    }


    override fun findCurrentUser(): User? {
        return userRepository.findCurrentUser()
    }

    override fun findByRole(role: UserRole, page: Int, maxResults: Int, isDesc: Boolean): List<User> {
        return ArrayList(roleRepository.findByUserRole(role)!!.users!!)
    }

    override fun findAllUsers(page: Int, maxResults: Int, isDesc: Boolean): List<User> {
        val pageable = getPageable(page, maxResults, isDesc)
        return userRepository.findAll(pageable)
    }

    private fun toUserDto(user: User?): UserDto? {
        if (user == null) {
            return null
        }
        val userDto = UserDto()
        BeanUtils.copyProperties(user, userDto)
        val roles = user.roles.stream()
                .map { (_, roleType) -> roleType!!.toString() }
                .collect(Collectors.toList())
        userDto.roles = roles
        userDto.roleType = user.roleType!!.toString()
        return userDto
    }

    private fun addRolesToUser(user: User, roles: List<String>) {
        if (roles.isEmpty()) {
            return
        }
        user.roles.clear()
        val level = 0
        var roleType: UserRole? = null
        for (r in roles) {
            val role = roleRepository.findByUserRoleString(r) ?: continue
            if (role.level > level) {
                roleType = role.roleType
            }
            user.roles.add(role)
        }
        if (roleType != null) {
            user.roleType = roleType
        }
    }

    private fun equalLists(one: List<String>?, two: List<String>?): Boolean {
        if (one == null && two == null) {
            return true
        }

        if (one == null && two != null
                || one != null && two == null
                || one!!.size != two!!.size) {
            return false
        }

        return one.sorted() == two.sorted()
    }

    private fun getPageable(page: Int, maxResults: Int, isDesc: Boolean): Pageable {
        val direction = if (isDesc) Sort.Direction.DESC else Sort.Direction.ASC
        return PageRequest.of(page, maxResults, direction, "joinedAt")
    }
}
