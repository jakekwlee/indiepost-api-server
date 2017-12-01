package com.indiepost.dto;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class AdminInitialData {

    private List<UserDto> creators;

    private List<String> creatorNames;

    private UserDto currentUser;

    private List<CategoryDto> categories;

    public List<UserDto> getCreators() {
        return creators;
    }

    public void setCreators(List<UserDto> creators) {
        this.creators = creators;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<String> getCreatorNames() {
        return creatorNames;
    }

    public void setCreatorNames(List<String> creatorNames) {
        this.creatorNames = creatorNames;
    }
}
