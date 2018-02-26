package com.indiepost.dto;

public class UserUpdateDto {

    private boolean isNew;

    private UserDto user;

    public UserUpdateDto() {
    }

    public UserUpdateDto(boolean isNew, UserDto user) {
        this.isNew = isNew;
        this.user = user;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
