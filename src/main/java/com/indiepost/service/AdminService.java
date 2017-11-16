package com.indiepost.service;

import com.indiepost.dto.AdminInitialData;
import com.indiepost.dto.UserDto;
import com.indiepost.enums.Types.UserRole;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {

    List<UserDto> getUserDtoList(UserRole role);

    List<UserDto> getUserDtoList(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUserDto();

    AdminInitialData buildInitialResponse();
}
