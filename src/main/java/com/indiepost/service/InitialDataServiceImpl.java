package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.StaticPageDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.stat.BannerDto;
import com.indiepost.enums.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final StaticPageService staticPageService;

    private final CampaignService campaignService;

    private final AppConfig config;

    @Autowired
    public InitialDataServiceImpl(CategoryService categoryService, UserService userService,
                                  PostService postService, StaticPageService staticPageService, AppConfig config, CampaignService campaignService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.postService = postService;
        this.staticPageService = staticPageService;
        this.config = config;
        this.campaignService = campaignService;
    }

    @Override
    public InitialData getInitialData(boolean withLatestPosts) {
        InitialData initialData = new InitialData();
        initialData.setCategories(categoryService.getDtoList());
        initialData.setCurrentUser(userService.getCurrentUserDto());
        initialData.setWithLatestPosts(withLatestPosts);
        Page<StaticPageDto> page = staticPageService.find(
                Types.PostStatus.PUBLISH, PageRequest.of(0, 100, Sort.Direction.DESC, "displayOrder"));
        initialData.setPages(page.getContent());

        initialData.setSplash(postService.findSplashPost());
        initialData.setFeatured(postService.findFeaturePost());
        initialData.setPickedPosts(postService.findPickedPosts());

        if (withLatestPosts) {
            initialData.setPosts(postService.find(0, config.getFetchCount(), true));
        }

        List<BannerDto> banners = campaignService.findBannersOnCampaignPeriod();
        if (banners != null && banners.size() > 0) {
            initialData.setBanners(banners);
        }

        LocalDateTime now = LocalDateTime.now();
        List<PostSummaryDto> topPosts = postService.findTopRatedPosts(now.minusDays(8L), now, 10);
        initialData.setTopPosts(topPosts);
        return initialData;
    }
}
