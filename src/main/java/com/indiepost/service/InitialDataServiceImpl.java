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
        List<PostSummaryDto> posts = postService.findByStatus(PostEnum.Status.PUBLISH, 0, 20, true);

        query.setSplash(true);
        List<PostSummaryDto> specialPosts = postService.findByQuery(query, 0, 1, true);

        query.setSplash(false);
        query.setFeatured(true);
        this.mergePostSummaryDtoListBtoA(specialPosts,
                postService.findByQuery(query, 0, 4, true)
        );

        query.setFeatured(false);
        query.setPicked(true);
        this.mergePostSummaryDtoListBtoA(specialPosts,
                postService.findByQuery(query, 0, 10, true)
        );

        this.mergePostSummaryDtoListBtoA(posts, specialPosts);
        initialResponse.setPosts(posts);
        return initialResponse;
    }

    private void mergePostSummaryDtoListBtoA(List<PostSummaryDto> dtoListA, List<PostSummaryDto> dtoListB) {
        if (dtoListB.size() > 0) {
            for (PostSummaryDto postB : dtoListB) {
                boolean isDuplicate = false;
                for (PostSummaryDto postA : dtoListA) {
                    isDuplicate = postB.getId().equals(postA.getId());
                    if (isDuplicate) {
                        break;
                    }
                }
                if (!isDuplicate) {
                    dtoListA.add(postB);
                }
            }
        }
    }
}
