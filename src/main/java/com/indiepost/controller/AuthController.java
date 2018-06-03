package com.indiepost.controller;

import com.indiepost.dto.AccountCredentials;
import com.indiepost.dto.LoginSuccessResponse;
import com.indiepost.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jake on 7/22/17.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    public AuthController(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @PostMapping("/login")
    public LoginSuccessResponse login(HttpServletResponse response, @RequestBody AccountCredentials credentials) {
        return tokenAuthenticationService.authenticate(response, credentials);
    }


    @GetMapping("/renew")
    public LoginSuccessResponse renew(HttpServletResponse response) {
        return tokenAuthenticationService.renewAuthentication(response);
    }
}
