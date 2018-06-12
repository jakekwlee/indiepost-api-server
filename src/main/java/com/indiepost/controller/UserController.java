package com.indiepost.controller;

import com.indiepost.dto.UserDto;
import com.indiepost.dto.UserProfileDto;
import com.indiepost.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{username}")
    public UserProfileDto sync(@PathVariable String username, @RequestBody UserDto userDto) {
        if (!userDto.getUsername().equals(username)) {
            //TODO handle error
            return null;
        }
        return userService.sync(userDto);
    }
}
