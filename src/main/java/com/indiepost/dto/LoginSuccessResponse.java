package com.indiepost.dto;

import com.indiepost.dto.user.UserDto;

public class LoginSuccessResponse {
    private UserDto user;

    private String token;

    public LoginSuccessResponse() {
    }

    public LoginSuccessResponse(UserDto user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
