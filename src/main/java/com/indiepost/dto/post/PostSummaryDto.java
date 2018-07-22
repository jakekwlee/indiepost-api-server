package com.indiepost.dto.post;

import com.indiepost.dto.Highlight;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by jake on 17. 1. 21.
 */
public class PostSummaryDto implements Serializable {

    private Long id;

    private boolean featured;

    private boolean picked;

    private boolean splash;

    private String title;

    private String excerpt;

    private String displayName;

    private Instant publishedAt;

    private Instant modifiedAt;

    private boolean showLastUpdated;

    private ImageSet titleImage;

    private PostStatus status;

    private String categoryName;

    private int commentsCount;

    private Instant lastRead;

    private Instant bookmarked;

    private boolean interactionFetched;

    private Highlight highlight;

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isShowLastUpdated() {
        return showLastUpdated;
    }

    public void setShowLastUpdated(boolean showLastUpdated) {
        this.showLastUpdated = showLastUpdated;
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

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Instant getLastRead() {
        return lastRead;
    }

    public void setLastRead(Instant lastRead) {
        this.lastRead = lastRead;
    }

    public Instant getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Instant bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isInteractionFetched() {
        return interactionFetched;
    }

    public void setInteractionFetched(boolean interactionFetched) {
        this.interactionFetched = interactionFetched;
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
                .append(categoryName)
                .append(getStatus())
                .toHashCode();
    }

}
