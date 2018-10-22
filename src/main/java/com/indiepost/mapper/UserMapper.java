package com.indiepost.mapper;

import com.indiepost.dto.user.UserDto;
import com.indiepost.enums.Types;
import com.indiepost.model.User;

import java.time.ZoneId;

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
        user.setUpdatedAt(userDto.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
        if (userDto.getGender() == null) {
            userDto.setGender("UNIDENTIFIED");
        }
        Types.UserGender gender = Types.UserGender.valueOf(userDto.getGender());
        user.setGender(gender);
        // TODO do not map roles. why???
    }

}
