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

    private List<Long> topRated;

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

    public List<Long> getTopRated() {
        return topRated;
    }

    public void setTopRated(List<Long> topRated) {
        this.topRated = topRated;
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
