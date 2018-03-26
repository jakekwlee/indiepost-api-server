package com.indiepost.dto;

import java.time.LocalDateTime;

import static com.indiepost.enums.Types.PostStatus;

/**
 * Created by jake on 17. 1. 10.
 */
public class PostQuery {

    private int page;

    private int maxResults;

    private Long authorId;

    private Long editorId;

    private Long categoryId;

    private String categorySlug;

    private LocalDateTime createdAfter;

    private LocalDateTime createdBefore;

    private LocalDateTime modifiedAfter;

    private LocalDateTime modifiedBefore;

    private LocalDateTime publishedAfter;

    private LocalDateTime publishedBefore;

    private PostStatus status = PostStatus.PUBLISH;

    private boolean featured = false;

    private boolean picked = false;

    private boolean splash = false;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public LocalDateTime getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(LocalDateTime createdAfter) {
        this.createdAfter = createdAfter;
    }

    public LocalDateTime getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(LocalDateTime createdBefore) {
        this.createdBefore = createdBefore;
    }

    public LocalDateTime getModifiedAfter() {
        return modifiedAfter;
    }

    public void setModifiedAfter(LocalDateTime modifiedAfter) {
        this.modifiedAfter = modifiedAfter;
    }

    public LocalDateTime getModifiedBefore() {
        return modifiedBefore;
    }

    public void setModifiedBefore(LocalDateTime modifiedBefore) {
        this.modifiedBefore = modifiedBefore;
    }

    public LocalDateTime getPublishedAfter() {
        return publishedAfter;
    }

    public void setPublishedAfter(LocalDateTime publishedAfter) {
        this.publishedAfter = publishedAfter;
    }

    public LocalDateTime getPublishedBefore() {
        return publishedBefore;
    }

    public void setPublishedBefore(LocalDateTime publishedBefore) {
        this.publishedBefore = publishedBefore;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }
}
