package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.mapper.TagMapper;
import com.indiepost.mapper.UserMapper;
import com.indiepost.model.*;
import com.indiepost.dto.CategoryDto;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.UserDto;
import com.indiepost.dto.response.AdminInitResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private AdminPostService adminPostService;

    private CategoryService categoryService;

    private UserService userService;

    private TagService tagService;

    private UserMapper userMapper;

    private TagMapper tagMapper;

    @Autowired
    public AdminServiceImpl(AdminPostService adminPostService, CategoryService categoryService,
                            TagService tagService, UserService userService, UserMapper userMapper, TagMapper tagMapper) {
        this.adminPostService = adminPostService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public AdminInitResponseDto buildInitialResponse() {
        User currentUser = userService.getCurrentUser();
        AdminInitResponseDto adminInitResponseDto = new AdminInitResponseDto();
        adminInitResponseDto.setCurrentUser(userMapper.userToUserDto(currentUser));
        adminInitResponseDto.setAuthors(getUserDtoList(UserEnum.Roles.Author));
        adminInitResponseDto.setCategories(getAllCategoryDtoList());
        adminInitResponseDto.setTags(getAllTagDtoList());
        adminInitResponseDto.setAuthorNames(adminPostService.findAllDisplayNames());
        return adminInitResponseDto;
    }

    @Override
    public List<TagDto> getAllTagDtoList() {
        List<Tag> tagList = tagService.findAll();
        return tagMapper.tagListToTagDtoList(tagList);
    }

    @Override
    public List<UserDto> getUserDtoList(UserEnum.Roles role) {
        List<User> authors = userService.findByRolesEnum(role, 1, 1000000, true);
        return userListToUserDtoList(authors);
    }

    @Override
    public List<UserDto> getUserDtoList(int page, int maxResults, boolean isDesc) {
        List<User> userList = userService.findAllUsers(page, maxResults, isDesc);
        return userListToUserDtoList(userList);
    }

    @Override
    public UserDto getCurrentUserDto() {
        User currentUser = userService.getCurrentUser();
        return userMapper.userToUserDto(currentUser);
    }

    @Override
    public List<CategoryDto> getAllCategoryDtoList() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setSlug(category.getSlug());
            categoryDto.setName(category.getName());
            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }

    private List<UserDto> userListToUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userMapper.userToUserDto(user));
        }
        return userDtoList;
    }
}
