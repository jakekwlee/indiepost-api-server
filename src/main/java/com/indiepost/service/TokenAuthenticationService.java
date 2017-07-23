package com.indiepost.service;

import com.indiepost.dto.AccountCredentials;
import com.indiepost.dto.UserDto;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jake on 7/22/17.
 */
public interface TokenAuthenticationService {
    UserDto authenticate(HttpServletResponse res, AccountCredentials credentials);

    UserDto renewAuthentication(HttpServletResponse res);
}