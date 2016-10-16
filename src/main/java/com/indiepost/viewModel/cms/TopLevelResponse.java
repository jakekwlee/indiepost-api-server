package com.indiepost.viewModel.cms;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class TopLevelResponse {

    private static final long serialVersionUID = 1L;

    private List<List<PostMeta>> published;

    private List<List<PostMeta>> booked;

    private List<List<PostMeta>> queued;

    private List<List<PostMeta>> draft;

    private List<List<PostMeta>> deleted;

    private List<UserMeta> authors;

    private UserMeta currentUser;

    private List<TagMeta> tags;

    private PaginationMeta pagination;

    private List<CategoryMeta> categories;

    public List<List<PostMeta>> getPublished() {
        return published;
    }

    public void setPublished(List<List<PostMeta>> published) {
        this.published = published;
    }

    public List<List<PostMeta>> getBooked() {
        return booked;
    }

    public void setBooked(List<List<PostMeta>> booked) {
        this.booked = booked;
    }

    public List<List<PostMeta>> getQueued() {
        return queued;
    }

    public void setQueued(List<List<PostMeta>> queued) {
        this.queued = queued;
    }

    public List<List<PostMeta>> getDraft() {
        return draft;
    }

    public void setDraft(List<List<PostMeta>> draft) {
        this.draft = draft;
    }

    public List<List<PostMeta>> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<List<PostMeta>> deleted) {
        this.deleted = deleted;
    }

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

    public PaginationMeta getPagination() {
        return pagination;
    }

    public void setPagination(PaginationMeta pagination) {
        this.pagination = pagination;
    }

    public List<CategoryMeta> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryMeta> categories) {
        this.categories = categories;
    }
}
