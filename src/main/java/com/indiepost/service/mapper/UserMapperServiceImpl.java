package com.indiepost.service.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.model.User;
import org.springframework.stereotype.Service;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
public class UserMapperServiceImpl implements UserMapperService {
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
