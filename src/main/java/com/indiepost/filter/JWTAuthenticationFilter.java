package com.indiepost.filter;

import com.indiepost.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final JwtConfig jwtConfig;

    @Value("${jwt.token:ThisIsSecret}")
    private String secretKey;

    @Inject
    public JWTAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(jwtConfig.getHttpHeaderName());
        if (token == null || req.getRequestURI().equals("/api/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.replace(jwtConfig.getTokenPrefix(), ""))
                .getBody();

        String user = claims.getSubject();
        if (user == null) {
            return;
        }
        ArrayList<String> authList = claims.get("roles", ArrayList.class);

        Set<GrantedAuthority> grantedAuthorities = authList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities));
        chain.doFilter(request, response);
    }
}