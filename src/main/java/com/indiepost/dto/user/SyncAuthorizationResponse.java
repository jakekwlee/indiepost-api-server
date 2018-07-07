package com.indiepost.dto.user;

public class SyncAuthorizationResponse {

    private boolean isNewUser;

    private UserDto user;

    public SyncAuthorizationResponse() {
    }

    public SyncAuthorizationResponse(boolean isNewUser, UserDto user) {
        this.isNewUser = isNewUser;
        this.user = user;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
