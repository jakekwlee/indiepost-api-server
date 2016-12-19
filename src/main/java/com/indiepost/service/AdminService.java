package com.indiepost.service;

import dto.CategoryDto;
import dto.TagDto;
import dto.UserDto;
import dto.response.AdminDataTableItem;
import dto.response.AdminInitResponseDto;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {
    List<AdminDataTableItem> getAdminPostListItemDtos(int page, int maxResults, boolean isDesc);

    List<AdminDataTableItem> getLastUpdated(Date date);

    List<TagDto> getAllTagDtos();

    List<UserDto> getAllAuthorsUserDtos();

    List<UserDto> getAllUserDtos(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUserDto();

    List<CategoryDto> getAlllCategoryDtos();

    AdminInitResponseDto getInitialResponse();
}
