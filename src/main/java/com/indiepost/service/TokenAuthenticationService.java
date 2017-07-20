package com.indiepost.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/18/17.
 */
public interface TokenAuthenticationService {
    long EXPIRATION_TIME = 864_000_000; // 10 days
    String SECRET = "ThisIsASecret";
    String TOKEN_PREFIX = "TwinPeaks";
    String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse res, Authentication auth) throws IOException {
        List<String> authorities = auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        Claims claims = Jwts.claims()
                .setSubject(auth.getName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        claims.put("roles", authorities);

        String JWT = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
        res.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        res.setStatus(HttpServletResponse.SC_OK);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roles", authorities.toArray());
        jsonObject.put("username", auth.getName());

        Writer writer = res.getWriter();
        jsonObject.write(writer);
        writer.flush();
        writer.close();
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        String user = claims.getSubject();
        ArrayList<String> authList = claims.get("roles", ArrayList.class);
        Set<GrantedAuthority> grantedAuthorities = getAuthorities(authList);

        return user != null ?
                new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities) :
                null;
    }

    static Set<GrantedAuthority> getAuthorities(List<String> authList) {
        return authList != null ? authList.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet()) :
                null;
    }
}