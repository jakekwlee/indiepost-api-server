package com.indiepost.service;

import com.indiepost.dto.UserDto;
import com.indiepost.dto.admin.AdminInitResponseDto;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.indiepost.mapper.UserMapper.userToUserDto;

/**
 * Created by jake on 10/8/16.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminPostService adminPostService;

    private final CategoryService categoryService;

    private final UserService userService;


    @Autowired
    public AdminServiceImpl(AdminPostService adminPostService, CategoryService categoryService,
                            UserService userService) {
        this.adminPostService = adminPostService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Override
    public AdminInitResponseDto buildInitialResponse() {
        User currentUser = userService.findCurrentUser();
        AdminInitResponseDto adminInitResponseDto = new AdminInitResponseDto();
        adminInitResponseDto.setCurrentUser(userToUserDto(currentUser));
        adminInitResponseDto.setAuthors(getUserDtoList(UserRole.Author));
        adminInitResponseDto.setCategories(categoryService.getDtoList());
        adminInitResponseDto.setAuthorNames(adminPostService.findAllBylineNames());
        return adminInitResponseDto;
    }

    @Override
    public List<UserDto> getUserDtoList(UserRole role) {
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
        User currentUser = userService.findCurrentUser();
        return userToUserDto(currentUser);
    }

    private List<UserDto> userListToUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(userToUserDto(user));
        }
        return userDtoList;
    }
}
