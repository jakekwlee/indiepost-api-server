package com.indiepost.service;

import com.indiepost.dto.user.SyncAuthorizationResponse;
import com.indiepost.dto.user.UserDto;
import com.indiepost.dto.user.UserProfileDto;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
public interface UserService {

    User findCurrentUser();

    void update(UserProfileDto userProfileDto);

    SyncAuthorizationResponse syncAuthorization(UserDto dto);

    List<User> findByRole(UserRole role, int page, int maxResults, boolean isDesc);

    List<User> findAllUsers(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUserDto();
}
