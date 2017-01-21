package com.indiepost.service.mapper;

import com.indiepost.dto.UserDto;
import com.indiepost.model.User;
import org.springframework.beans.BeanUtils;
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
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
