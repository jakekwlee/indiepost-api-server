package com.indiepost.dto.post;

import com.indiepost.dto.Highlight;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class AdminPostSummaryDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long originalId;

    private String title;

    private String status;

    private String displayName;

    private String categoryName;

    private String authorDisplayName;

    private String editorDisplayName;

    private Instant createdAt;

    private Instant publishedAt;

    private Instant modifiedAt;

    private List<String> Contributors = new ArrayList<>();

    private List<String> Tags = new ArrayList<>();

    private Highlight highlight;

    private boolean featured;

    private boolean picked;

    private boolean splash;

    private int likedCount;

    public List<String> getContributors() {
        return Contributors;
    }

    public void setContributors(List<String> contributors) {
        Contributors = contributors;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getEditorDisplayName() {
        return editorDisplayName;
    }

    public void setEditorDisplayName(String editorDisplayName) {
        this.editorDisplayName = editorDisplayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }
}
