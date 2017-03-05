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
