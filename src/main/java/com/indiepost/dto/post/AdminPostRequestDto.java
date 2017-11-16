package com.indiepost.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;
import com.indiepost.jackson.UtcStringToLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 11/19/16.
 */
public class AdminPostRequestDto {

    private Long id;

    private Long originalId;

    private String title;

    private String content;

    private String excerpt;

    private String displayName;

    private Long titleImageId;

    private String status;

    private Long categoryId;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    @JsonDeserialize(using = UtcStringToLocalDateTimeDeserializer.class)
    private LocalDateTime publishedAt;

    private boolean featured;

    private boolean picked;

    private boolean splash;

    private List<Long> profileIds;

    private List<Long> tagIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getTitleImageId() {
        return titleImageId;
    }

    public void setTitleImageId(Long titleImageId) {
        this.titleImageId = titleImageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
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

    public List<Long> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<Long> profileIds) {
        this.profileIds = profileIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}