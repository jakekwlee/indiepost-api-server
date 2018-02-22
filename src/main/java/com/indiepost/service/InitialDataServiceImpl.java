package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
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

    private final AppConfig config;

    @Inject
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService,
                                  PostService postService, PageService pageService, TagService tagService, AppConfig config) {
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
        initialData.setPages(pageService.find(Types.PostStatus.PUBLISH, 0, 100, false));

        initialData.setSplash(postService.findSplashPost());
        initialData.setFeatured(postService.findFeaturePost());
        initialData.setPickedPosts(postService.findPickedPosts());

        if (withLatestPosts) {
            initialData.setPosts(postService.find(0, config.getFetchCount(), true));
        }

        LocalDateTime now = LocalDateTime.now();
        List<PostSummaryDto> topPosts = postService.findTopRatedPosts(now.minusDays(8L), now, 10L);
        initialData.setTopPosts(topPosts);
        return initialData;
    }
}
