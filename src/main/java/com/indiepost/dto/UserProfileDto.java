package com.indiepost.dto;

public class UserProfileDto {

    private boolean isNewlyJoined;

    private UserDto user;

    public UserProfileDto() {
    }

    public UserProfileDto(boolean isNewlyJoined, UserDto user) {
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
