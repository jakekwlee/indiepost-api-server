package com.indiepost.mapper;

import com.indiepost.model.User;
import com.indiepost.dto.UserDto;

/**
 * Created by jake on 17. 1. 14.
 */
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
