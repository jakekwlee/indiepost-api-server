package com.indiepost.controller.api;

import com.indiepost.dto.UserDto;
import com.indiepost.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{username}")
    public UserDto createOrUpdateUser(@RequestBody UserDto userDto) {
        //TODO
        return null;
    }
}
