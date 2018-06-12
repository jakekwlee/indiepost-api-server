package com.indiepost.service;

import com.indiepost.dto.UserDto;
import com.indiepost.dto.UserProfileDto;
import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
public interface UserService {

    void save(User user);

    void update(User user);

    void delete(User user);

    User findCurrentUser();

    User findById(Long id);

    User findByUsername(String username);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    List<User> findByState(UserState state, int page, int maxResults, boolean isDesc);

    List<User> findByGender(UserGender gender, int page, int maxResults, boolean isDesc);

    List<User> findByRolesEnum(UserRole role, int page, int maxResults, boolean isDesc);

    List<User> findAllUsers(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUserDto();

    UserDto getUserDto(User user);

    UserDto getUserDto(Long id);

    UserDto getUserDto(String username);

    List<UserDto> getDtoList(List<User> users);

    List<UserDto> getDtoList(int page, int maxResults, boolean isDesc);

    List<UserDto> getDtoList(UserRole role, int page, int maxResults, boolean isDesc);

    UserProfileDto sync(UserDto dto);
}
