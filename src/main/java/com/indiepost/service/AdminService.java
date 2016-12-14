package com.indiepost.service;

import dto.CategoryDto;
import dto.TagDto;
import dto.UserDto;
import dto.response.*;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {
    List<AdminPostListItemDto> getAllSimplifiedPosts(int page, int maxResults, boolean isDesc);

    List<AdminPostListItemDto> getLastUpdated(Date date);

    List<TagDto> getAllSimplifiedTags();

    List<UserDto> getAllSimplifiedAuthors();

    List<UserDto> getAllUsersMeta(int page, int maxResults, boolean isDesc);

    UserDto getCurrentUser();

    List<CategoryDto> getAllSimplifiedCategories();

    AdminInitResponseDto getInitialResponse();
}
