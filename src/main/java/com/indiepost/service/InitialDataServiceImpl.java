package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService,
                                  PostService postService, PageService pageService, AppConfig config) {
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
        List<PostSummary> posts = postService.findByQuery(query, 0, 1, true);

        query.setSplash(false);
        query.setFeatured(true);
        this.mergePostSummaryListBtoA(posts,
                postService.findByQuery(query, 0, 1, true)
        );

        query.setFeatured(false);
        query.setPicked(true);
        this.mergePostSummaryListBtoA(posts,
                postService.findByQuery(query, 0, 8, true)
        );

        if (withLatestPosts) {
            this.mergePostSummaryListBtoA(posts,
                    postService.findAll(0, config.getFetchCount(), true)
            );
        }

        LocalDateTime now = LocalDateTime.now();
        List<PostSummary> topRatedPosts = postService.getTopRatedPosts(now.minusDays(10L), now, 10L);
        this.mergePostSummaryListBtoA(posts, topRatedPosts);
        initialData.setPosts(posts);
        initialData.setTopRated(
                topRatedPosts.stream()
                        .map(postSummary -> postSummary.getId())
                        .collect(Collectors.toList())
        );
        return initialData;
    }

    private void mergePostSummaryListBtoA(List<PostSummary> dtoListA, List<PostSummary> dtoListB) {
        if (dtoListB.size() > 0) {
            for (PostSummary postB : dtoListB) {
                boolean isDuplicate = false;
                for (PostSummary postA : dtoListA) {
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
