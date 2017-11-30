package com.indiepost.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;

/**
 * Created by jake on 11/19/16.
 */
public class AdminPostResponseDto extends AdminPostRequestDto {

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime modifiedAt;

    private ImageSetDto titleImage;

    private int bookmarkCount = 0;

    private Long creatorId;

    private Long modifiedUserId;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ImageSetDto getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(ImageSetDto titleImage) {
        this.titleImage = titleImage;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }
}
