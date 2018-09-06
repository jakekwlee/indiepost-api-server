package com.indiepost.dto;

import com.indiepost.dto.analytics.BannerDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.user.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class InitialData {

    private List<CategoryDto> categories;

    private UserDto currentUser;

    private Page<PostSummaryDto> posts;

    private List<PostSummaryDto> topPosts;

    private List<PostSummaryDto> pickedPosts;

    private PostSummaryDto splash;

    private PostSummaryDto featured;

    private List<StaticPageDto> staticPages;

    private List<BannerDto> banners;

    private boolean withLatestPosts;

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public Page<PostSummaryDto> getPosts() {
        return posts;
    }

    public void setPosts(Page<PostSummaryDto> posts) {
        this.posts = posts;
    }

    public List<PostSummaryDto> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<PostSummaryDto> topPosts) {
        this.topPosts = topPosts;
    }

    public List<PostSummaryDto> getPickedPosts() {
        return pickedPosts;
    }

    public void setPickedPosts(List<PostSummaryDto> pickedPosts) {
        this.pickedPosts = pickedPosts;
    }

    public PostSummaryDto getSplash() {
        return splash;
    }

    public void setSplash(PostSummaryDto splash) {
        this.splash = splash;
    }

    public PostSummaryDto getFeatured() {
        return featured;
    }

    public void setFeatured(PostSummaryDto featured) {
        this.featured = featured;
    }

    public boolean isWithLatestPosts() {
        return withLatestPosts;
    }

    public void setWithLatestPosts(boolean withLatestPosts) {
        this.withLatestPosts = withLatestPosts;
    }

    public List<StaticPageDto> getStaticPages() {
        return staticPages;
    }

    public void setStaticPages(List<StaticPageDto> staticPages) {
        this.staticPages = staticPages;
    }

    public List<BannerDto> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerDto> banners) {
        this.banners = banners;
    }
}
