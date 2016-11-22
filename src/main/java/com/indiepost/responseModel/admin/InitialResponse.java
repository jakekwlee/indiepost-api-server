package com.indiepost.responseModel.admin;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class InitialResponse {

    private static final long serialVersionUID = 1L;

    private List<SimplifiedUser> authors;

    private List<String> authorNames;

    private SimplifiedUser currentUser;

    private List<SimplifiedTag> tags;

    private List<SimplifiedCategory> categories;

    public List<SimplifiedUser> getAuthors() {
        return authors;
    }

    public void setAuthors(List<SimplifiedUser> authors) {
        this.authors = authors;
    }

    public SimplifiedUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SimplifiedUser currentUser) {
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
