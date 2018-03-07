package com.indiepost.dto.stat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 5. 10.
 */
public class PostStatDto {

    private Long id;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime publishedAt;

    private String title;

    private String category;

    private String author;

    private Long pageviews;

    private Long uniquePageviews;

    private Long legacyPageviews;

    private Long legacyUniquePageviews;

    public Long getLegacyPageviews() {
        return legacyPageviews;
    }

    public void setLegacyPageviews(Long legacyPageviews) {
        this.legacyPageviews = legacyPageviews;
    }

    public Long getLegacyUniquePageviews() {
        return legacyUniquePageviews;
    }

    public void setLegacyUniquePageviews(Long legacyUniquePageviews) {
        this.legacyUniquePageviews = legacyUniquePageviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getPageviews() {
        return pageviews;
    }

    public void setPageviews(Long pageviews) {
        this.pageviews = pageviews;
    }

    public Long getUniquePageviews() {
        return uniquePageviews;
    }

    public void setUniquePageviews(Long uniquePageviews) {
        this.uniquePageviews = uniquePageviews;
    }
}
