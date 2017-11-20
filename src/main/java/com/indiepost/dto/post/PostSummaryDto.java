package com.indiepost.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 1. 21.
 */
public class PostSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long legacyPostId;

    private String title;

    private String bylineName;

    private String category;

    private String excerpt;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime publishedAt;

    private ImageSetDto titleImage;

    private int bookmarkCount;

    private boolean featured;

    private boolean picked;

    private boolean splash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBylineName() {
        return bylineName;
    }

    public void setBylineName(String bylineName) {
        this.bylineName = bylineName;
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

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PostSummaryDto)) {
            return false;
        }

        PostSummaryDto postSummaryDto = (PostSummaryDto) obj;
        return new EqualsBuilder()
                .append(id, postSummaryDto.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(category)
                .toHashCode();
    }
}
