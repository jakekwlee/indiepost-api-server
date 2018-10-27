package com.indiepost.service

import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.model.User
import com.indiepost.repository.UserRepository
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.inject.Inject

/**
 * Created by jake on 7/26/16.
 */
@Service
@Transactional(readOnly = true)
class CustomUserDetailService @Inject
constructor(private val userRepository: UserRepository) : UserDetailsService {

    @Inject
    @Throws(Exception::class)
    fun registerAuthentication(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(CustomUserDetailService(userRepository))
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        try {
            val user = userRepository.findByUsername(username)
                    ?: throw ResourceNotFoundException("User not found: $username")
            return org.springframework.security.core.userdetails.User(user.username, "indiepost0", this.getAuthorities(user))
        } catch (e: Exception) {
            e.printStackTrace()
            throw UsernameNotFoundException("User not found: $username")
        }

    }

    private fun getAuthorities(user: User): Set<GrantedAuthority> {
        val authorities = HashSet<GrantedAuthority>()
        for ((_, roleType) in user.roles) {
            val grantedAuthority = SimpleGrantedAuthority(roleType!!.toString())
            authorities.add(grantedAuthority)
        }
        return authorities
    }
}
