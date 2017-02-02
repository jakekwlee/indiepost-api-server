package com.indiepost.service;

import com.indiepost.dto.InitialResponse;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.enums.PostEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        PostQuery query = new PostQuery();
        List<PostSummaryDto> posts = postService.findByStatus(PostEnum.Status.PUBLISH, 0, 36, true);
        query.setSplash(true);
        posts.addAll(
                postService.findByQuery(query, 0, 1, true)
        );
        query.setSplash(false);
        query.setFeatured(true);
        posts.addAll(
                postService.findByQuery(query, 0, 4, true)
        );
        query.setFeatured(false);
        query.setPicked(true);
        posts.addAll(
                postService.findByQuery(query, 0, 12, true)
        );
        initialResponse.setPosts(posts);
        return initialResponse;
    }
}
