package com.indiepost.mapper;

import com.indiepost.dto.user.UserDto;
import com.indiepost.enums.Types;
import com.indiepost.model.User;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

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

    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setDisplayName(user.getDisplayName());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender().toString());
        userDto.setPicture(user.getPicture());
        userDto.setProfile(user.getProfile());
        userDto.setJoinedAt(localDateTimeToInstant(user.getJoinedAt()));
        userDto.setUpdatedAt(localDateTimeToInstant(user.getUpdatedAt()));
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
        userDto.setRoles(roles);
        return userDto;
    }
}
