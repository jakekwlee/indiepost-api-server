package com.indiepost.dto;

import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 1. 21.
 */
public class PostSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long legacyPostId;

    private boolean featured;

    private boolean picked;

    private boolean splash;

    private String title;

    private String excerpt;

    private String displayName;

    private LocalDateTime publishedAt;

    private ImageSet titleImage;

    private Long titleImageId;

    private PostStatus status;

    private Long categoryId;

    private String categoryName;

    private int commentsCount;

    private int likesCount;

    private Highlight highlight;

    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    public PostSummary() {
    }

    public PostSummary(Long id, Long legacyPostId, boolean featured, boolean picked, boolean splash,
                       String title, String excerpt, String displayName, LocalDateTime publishedAt, ImageSet titleImage, Long titleImageId,
                       PostStatus status, Long categoryId, String categoryName, int commentsCount, int likesCount) {
        this.id = id;
        this.legacyPostId = legacyPostId;
        this.featured = featured;
        this.picked = picked;
        this.splash = splash;
        this.title = title;
        this.excerpt = excerpt;
        this.displayName = displayName;
        this.publishedAt = publishedAt;
        this.titleImage = titleImage;
        this.titleImageId = titleImageId;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ImageSet getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(ImageSet titleImage) {
        this.titleImage = titleImage;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Long getTitleImageId() {
        return titleImageId;
    }

    public void setTitleImageId(Long titleImageId) {
        this.titleImageId = titleImageId;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public Long getLegacyPostId() {
        return legacyPostId;
    }

    public void setLegacyPostId(Long legacyPostId) {
        this.legacyPostId = legacyPostId;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PostSummary)) {
            return false;
        }

        PostSummary postSummary = (PostSummary) obj;
        return new EqualsBuilder()
                .append(id, postSummary.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(categoryName)
                .append(getStatus())
                .toHashCode();
    }
}
