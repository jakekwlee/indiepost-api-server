package com.indiepost.model.response;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class AdminInitialResponse {

    private static final long serialVersionUID = 1L;

    private List<AdminUserResponse> authors;

    private List<String> authorNames;

    private AdminUserResponse currentUser;

    private List<SimplifiedTag> tags;

    private List<SimplifiedCategory> categories;

    public List<AdminUserResponse> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AdminUserResponse> authors) {
        this.authors = authors;
    }

    public AdminUserResponse getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AdminUserResponse currentUser) {
        this.currentUser = currentUser;
    }

    public List<SimplifiedTag> getTags() {
        return tags;
    }

    public void setTags(List<SimplifiedTag> tags) {
        this.tags = tags;
    }

    public List<SimplifiedCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SimplifiedCategory> categories) {
        this.categories = categories;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }
}
