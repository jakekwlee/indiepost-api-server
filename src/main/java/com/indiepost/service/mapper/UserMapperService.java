package com.indiepost.service.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.model.User;

/**
 * Created by jake on 17. 1. 14.
 */
public interface UserMapperService {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
