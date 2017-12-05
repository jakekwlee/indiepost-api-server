package com.indiepost.service;

import com.indiepost.config.JwtConfig;
import com.indiepost.dto.AccountCredentials;
import com.indiepost.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/22/17.
 */
@Service
public class JwtTokenAuthenticationService implements TokenAuthenticationService {
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @Value("${jwt.token:ThisIsSecret}")
    private String secretKey;

    @Inject
    public JwtTokenAuthenticationService(JwtConfig jwtConfig, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public UserDto authenticate(HttpServletResponse res, AccountCredentials credentials) throws AuthenticationException {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = makeToken(auth.getName(), auth, credentials.isRememberMe());
            res.addHeader(jwtConfig.getHttpHeaderName(), jwtConfig.getTokenPrefix() + " " + token);
            res.setStatus(HttpServletResponse.SC_OK);
            return userService.getCurrentUserDto();
        } catch (BadCredentialsException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }

    @Override
    public UserDto renewAuthentication(HttpServletResponse res) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            String token = makeToken(auth.getName(), auth, false);
            res.addHeader(jwtConfig.getHttpHeaderName(), jwtConfig.getTokenPrefix() + " " + token);
            res.setStatus(HttpServletResponse.SC_OK);
            return userService.getCurrentUserDto();
        }
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return null;
    }

    private String makeToken(String subject, Authentication auth, boolean rememberMe) {
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        long currentTime = System.currentTimeMillis();
        Date expiration = rememberMe ? new Date(currentTime + jwtConfig.getRememberMeExpiration()) :
                new Date(currentTime + jwtConfig.getTokenExpiration());

        Claims claims = Jwts.claims()
                .setSubject(subject)
                .setExpiration(expiration);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
