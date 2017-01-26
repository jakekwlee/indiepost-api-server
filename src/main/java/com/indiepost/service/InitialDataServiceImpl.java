package com.indiepost.service;

import com.indiepost.dto.InitialResponse;
import com.indiepost.dto.PostQuery;
import com.indiepost.enums.PostEnum;
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

    private final PostService postService;

    @Autowired
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService, PostService postService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.postService = postService;
    }


    @Override
    public InitialResponse getInitialData() {
        InitialResponse initialResponse = new InitialResponse();
        initialResponse.setCategories(categoryService.getDtoList());
        initialResponse.setCurrentUser(userService.getCurrentUserDto());
        PostQuery featuredPostsQuery = new PostQuery();
        PostQuery editorsPicksQuery = new PostQuery();
        featuredPostsQuery.setFeatured(true);
        editorsPicksQuery.setPicked(true);
        initialResponse.setPosts(
                postService.findByStatus(PostEnum.Status.PUBLISH, 0, 30, true)
        );
        initialResponse.setEditorsPicks(
                postService.findByQuery(editorsPicksQuery, 0, 10, true)
        );
        initialResponse.setFeaturedPosts(
                postService.findByQuery((featuredPostsQuery), 0, 10, true)
        );
        return initialResponse;
    }
}
