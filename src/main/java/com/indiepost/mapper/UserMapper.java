package com.indiepost.mapper;

import com.indiepost.dto.UserDto;
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
        return null;
    }

    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDto.setRoles(roles);
        return userDto;
    }
}
