package com.indiepost.service.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        BeanUtils.copyProperties(user, userDto);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDto.setRoles(roles);
        return userDto;
    }
}
