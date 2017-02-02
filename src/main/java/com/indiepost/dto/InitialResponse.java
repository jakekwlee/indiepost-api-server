package com.indiepost.dto;

import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class InitialResponse {

    private static final long serialVersionUID = 1L;

    private List<CategoryDto> categories;

    private UserDto currentUser;

    private List<PostSummaryDto> posts;

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

}
