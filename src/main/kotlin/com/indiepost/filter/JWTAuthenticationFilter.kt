package com.indiepost.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JWTAuthenticationFilter : GenericFilterBean() {

    private val tokenPrefix = "Bearer"

    private val authHeader = "Authorization"

    @Value("#{systemEnvironment['AUTH0_SECRET']}")
    private lateinit var signingSecret: String

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val token = req.getHeader(authHeader)
        if (token == null) {
            chain.doFilter(request, response)
            return
        }
        val accessToken = token.replace(tokenPrefix, "").replace(" ", "")
        val algorithm = Algorithm.HMAC256(signingSecret)
        val jwtVerifier = JWT.require(algorithm)
                .withIssuer("https://auth.indiepost.co.kr/")
                .withAudience("https://api.indiepost.co.kr/")
                .acceptLeeway(60)
                .build()
        val jwt: DecodedJWT
        try {
            jwt = jwtVerifier.verify(accessToken)
        } catch (te: TokenExpiredException) {
            log.warn("User requested with expired token: " + req.requestURI)
            chain.doFilter(request, response)
            return
        }

        val user = jwt.subject
        if (user == null) {
            log.warn("Abnormal token detected: subject is null")
            chain.doFilter(request, response)
            return
        }
        val authList = jwt.getClaim("https://www.indiepost.co.kr/roles").asList(String::class.java)

        val grantedAuthorities = authList.stream()
                .map { role -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toSet())

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, grantedAuthorities)
        chain.doFilter(request, response)
    }

    companion object {

        private val log = LoggerFactory.getLogger(JWTAuthenticationFilter::class.java)
    }
}