package com.indiepost.responseModel.admin;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class MetaInformation {

    private static final long serialVersionUID = 1L;

    private List<UserMeta> authors;

    private List<String> authorNames;

    private UserMeta currentUser;

    private List<TagMeta> tags;

    private List<CategoryMeta> categories;

    public List<UserMeta> getAuthors() {
        return authors;
    }

    public void setAuthors(List<UserMeta> authors) {
        this.authors = authors;
    }

    public UserMeta getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserMeta currentUser) {
        this.currentUser = currentUser;
    }

    public List<TagMeta> getTags() {
        return tags;
    }

    public void setTags(List<TagMeta> tags) {
        this.tags = tags;
    }

    public List<CategoryMeta> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryMeta> categories) {
        this.categories = categories;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }
}
