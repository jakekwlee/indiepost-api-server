package com.indiepost.dto;

import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class InitialData {

    private static final long serialVersionUID = 1L;

    private List<CategoryDto> categories;

    private UserDto currentUser;

    private List<PostSummary> posts;

    private List<PostSummary> topPosts;

    private List<PostSummary> pickedPosts;

    private PostSummary splash;

    private PostSummary featured;

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

    public List<PostSummary> getPosts() {
        return posts;
    }

    public void setPosts(List<PostSummary> posts) {
        this.posts = posts;
    }

    public List<PostSummary> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<PostSummary> topPosts) {
        this.topPosts = topPosts;
    }

    public List<PostSummary> getPickedPosts() {
        return pickedPosts;
    }

    public void setPickedPosts(List<PostSummary> pickedPosts) {
        this.pickedPosts = pickedPosts;
    }

    public PostSummary getSplash() {
        return splash;
    }

    public void setSplash(PostSummary splash) {
        this.splash = splash;
    }

    public PostSummary getFeatured() {
        return featured;
    }

    public void setFeatured(PostSummary featured) {
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
