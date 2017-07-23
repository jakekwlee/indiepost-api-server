package com.indiepost.controller.api;

import com.indiepost.dto.AccountCredentials;
import com.indiepost.dto.UserDto;
import com.indiepost.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jake on 7/22/17.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    public AuthController(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @PostMapping("/login")
    public UserDto login(HttpServletResponse response, @RequestBody AccountCredentials credentials) {
        return tokenAuthenticationService.authenticate(response, credentials);
    }


    @GetMapping("/renew")
    public UserDto renew(HttpServletResponse response) {
        return tokenAuthenticationService.renewAuthentication(response);
    }
}
