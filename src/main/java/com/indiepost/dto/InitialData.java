package com.indiepost.dto;

import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class InitialData {

    private static final long serialVersionUID = 1L;

    private List<CategoryDto> categories;

    private UserDto currentUser;

    private List<PostSummaryDto> posts;

    private List<PostSummaryDto> topPosts;

    private List<PostSummaryDto> pickedPosts;

    private PostSummaryDto splash;

    private PostSummaryDto featured;

    private List<PageDto> pages;

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

    public List<PostSummaryDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostSummaryDto> posts) {
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

    public List<PageDto> getPages() {
        return pages;
    }

    public void setPages(List<PageDto> pages) {
        this.pages = pages;
    }
}
