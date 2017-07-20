package com.indiepost.service;

import com.indiepost.config.WebappConfig;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
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

    private final PageService pageService;

    private final WebappConfig config;

    @Autowired
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService,
                                  PostService postService, PageService pageService, WebappConfig config) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.postService = postService;
        this.pageService = pageService;
        this.config = config;
    }

    @Override
    public InitialData getInitialData(boolean withLatestPosts) {
        InitialData initialData = new InitialData();
        initialData.setCategories(categoryService.getDtoList());
        initialData.setCurrentUser(userService.getCurrentUserDto());
        initialData.setWithLatestPosts(withLatestPosts);
        initialData.setPages(pageService.find(0, 100, false));

        PostQuery query = new PostQuery();
        query.setSplash(true);
        List<PostSummaryDto> posts = postService.findByQuery(query, 0, 1, true);

        query.setSplash(false);
        query.setFeatured(true);
        this.mergePostSummaryDtoListBtoA(posts,
                postService.findByQuery(query, 0, 1, true)
        );

        query.setFeatured(false);
        query.setPicked(true);
        this.mergePostSummaryDtoListBtoA(posts,
                postService.findByQuery(query, 0, 8, true)
        );

        if (withLatestPosts) {
            this.mergePostSummaryDtoListBtoA(posts,
                    postService.findAll(0, config.getFetchCount(), true)
            );
        }
        initialData.setPosts(posts);
        return initialData;
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
