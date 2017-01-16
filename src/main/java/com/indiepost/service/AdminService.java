package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.dto.CategoryDto;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.UserDto;
import com.indiepost.dto.response.AdminInitResponseDto;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {

    List<TagDto> getAllTagDtoList();

    List<UserDto> getUserDtoList(UserEnum.Roles role);

    List<UserDto> getUserDtoList(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUserDto();

    List<CategoryDto> getAllCategoryDtoList();

    AdminInitResponseDto buildInitialResponse();
}
