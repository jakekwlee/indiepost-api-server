package com.indiepost.service;

import com.indiepost.dto.InitialResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jake on 17. 1. 22.
 */
@Service
@Transactional(readOnly = true)
public class InitialDataServiceImpl implements InitialDataService {

    private final CategoryService categoryService;

    private final UserService userService;

    @Autowired
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @Override
    public InitialResponse getInitialData() {
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setCategories(categoryService.getDtoList());
        initialResponse.setCurrentUser(userService.getCurrentUserDto());
        return initialResponse;
    }
}
