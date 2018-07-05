package com.indiepost.dto.user;

public class SyncAuthorizationResponse {

    private boolean isNewlyJoined;

    private UserDto user;

    public SyncAuthorizationResponse() {
    }

    public SyncAuthorizationResponse(boolean isNewlyJoined, UserDto user) {
        this.isNewlyJoined = isNewlyJoined;
        this.user = user;
    }

    public boolean isNewlyJoined() {
        return isNewlyJoined;
    }

    public void setNewlyJoined(boolean newlyJoined) {
        isNewlyJoined = newlyJoined;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
