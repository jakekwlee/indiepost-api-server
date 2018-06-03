package com.indiepost.service;

import com.indiepost.dto.AccountCredentials;
import com.indiepost.dto.LoginSuccessResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by jake on 7/22/17.
 */
public interface TokenAuthenticationService {
    LoginSuccessResponse authenticate(HttpServletResponse res, AccountCredentials credentials);

    LoginSuccessResponse renewAuthentication(HttpServletResponse res);
}