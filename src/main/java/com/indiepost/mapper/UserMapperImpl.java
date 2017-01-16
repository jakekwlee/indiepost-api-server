package com.indiepost.mapper;

import com.indiepost.model.User;
import com.indiepost.dto.UserDto;
import org.springframework.stereotype.Component;

/**
 * Created by jake on 17. 1. 14.
 */
@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User userDtoToUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setDisplayName(user.getDisplayName());
        userDto.setEmail(user.getEmail());
        userDto.setBirthday(user.getBirthday());
        userDto.setGender(user.getGender());
        userDto.setJoinedAt(user.getJoinedAt());
        userDto.setPicture(user.getPicture());
        userDto.setProfile(user.getProfile());
        return userDto;
    }
}
