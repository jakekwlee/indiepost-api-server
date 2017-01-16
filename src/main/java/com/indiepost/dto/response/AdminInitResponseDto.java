package com.indiepost.dto.response;

import com.indiepost.dto.CategoryDto;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.UserDto;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class AdminInitResponseDto {

    private static final long serialVersionUID = 1L;

    private List<UserDto> authors;

    private List<String> authorNames;

    private UserDto currentUser;

    private List<TagDto> tags;

    private List<CategoryDto> categories;

    public List<UserDto> getAuthors() {
        return authors;
    }

    public void setAuthors(List<UserDto> authors) {
        this.authors = authors;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }
}
