package com.indiepost.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 17. 1. 14.
 */
public class UserMapper {

    public static User userDtoToUser(UserDto userDto) {
        User user = new User();
        userDtoToUser(userDto, user);
        return user;
    }

    public static void userDtoToUser(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setDisplayName(userDto.getDisplayName());
        user.setEmail(userDto.getEmail());
        user.setPicture(userDto.getPicture());
        user.setUpdatedAt(userDto.getUpdatedAt());
        if (userDto.getGender() == null) {
            userDto.setGender("UNIDENTIFIED");
        }
        Types.UserGender gender = Types.UserGender.valueOf(userDto.getGender());
        user.setGender(gender);
        // do not map roles
    }

    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDto.setGender(user.getGender().toString());
        userDto.setRoles(roles);
        return userDto;
    }
}
