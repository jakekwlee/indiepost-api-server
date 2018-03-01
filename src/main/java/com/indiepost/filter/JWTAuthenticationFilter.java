package com.indiepost.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.indiepost.config.JWTConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JWTConfig jwtConfig;

    @Inject
    public JWTAuthenticationFilter(JWTConfig jwtConfig) {
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
        String accessToken = token.replace(jwtConfig.getTokenPrefix(), "").replace(" ", "");
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSigningSecret());
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer("https://indiepost.auth0.com/")
                .withAudience("https://www.indiepost.co.kr/api")
                .acceptLeeway(60)
                .build();
        DecodedJWT jwt;
        try {
            jwt = jwtVerifier.verify(accessToken);
        } catch (TokenExpiredException te) {
            log.warn("User requested with expired token: " + req.getRequestURI());
            chain.doFilter(request, response);
            return;
        }
        String user = jwt.getSubject();
        if (user == null) {
            log.warn("Abnormal token detected: subject is null");
            chain.doFilter(request, response);
            return;
        }
        List<String> authList = jwt.getClaim("https://www.indiepost.co.kr/roles").asList(String.class);

        Set<GrantedAuthority> grantedAuthorities = authList.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities));
        chain.doFilter(request, response);
    }
}