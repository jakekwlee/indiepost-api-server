package com.indiepost.config

import com.indiepost.filter.JWTAuthenticationFilter
import com.indiepost.repository.UserRepository
import com.indiepost.service.CustomUserDetailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*
import javax.inject.Inject

/**
 * Created by jake on 7/26/16.
 */
@Configuration
@EnableWebSecurity
class SpringSecurityConfigurer @Inject constructor(
        private val userRepository: UserRepository,
        private val jwtAuthenticationFilter: JWTAuthenticationFilter) : WebSecurityConfigurerAdapter() {

    @Value("\${spring.profiles.active}")
    private lateinit var activeProfile: String

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/migration/**")
                .access("hasAuthority('Administrator')")
                .antMatchers("/admin/**")
                .access("hasAuthority('Editor')")
                .antMatchers(HttpMethod.PUT, "/user/**")
                .access("hasAuthority('User')")
                .antMatchers("/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun userDetailsServiceBean(): UserDetailsService {
        return CustomUserDetailService(userRepository)
    }

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Inject
    @Throws(Exception::class)
    fun registerAuthentication(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsServiceBean())
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.maxAge = 600L

        val allowedOrigins = ArrayList<String>()
        allowedOrigins.add("https://www.indiepost.co.kr")
        allowedOrigins.add("http://www.indiepost.co.kr")

        if (activeProfile == "dev") {
            allowedOrigins.add("http://localhost")
        }

        config.allowedOrigins = allowedOrigins
        config.allowedHeaders = Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "Referer")
        config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

}