package com.indiepost.dto.post;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.indiepost.enums.Types.PostStatus;
import static com.indiepost.utils.DateUtil.instantToLocalDateTime;

/**
 * Created by jake on 17. 1. 10.
 */
public class PostQuery {

    private String category;

    private String displayName;

    private String text;

    private PostStatus status;

    private Boolean featured;

    private Boolean picked;

    private Boolean splash;

    private LocalDateTime modifiedAfter;

    private LocalDateTime modifiedBefore;

    private LocalDateTime publishedAfter;

    private LocalDateTime publishedBefore;

    private PostQuery() {
    }

    public String getCategory() {
        return category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getText() {
        return text;
    }

    public PostStatus getStatus() {
        return status;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public Boolean getPicked() {
        return picked;
    }

    public Boolean getSplash() {
        return splash;
    }

    public LocalDateTime getModifiedAfter() {
        return modifiedAfter;
    }

    public LocalDateTime getModifiedBefore() {
        return modifiedBefore;
    }

    public LocalDateTime getPublishedAfter() {
        return publishedAfter;
    }

    public LocalDateTime getPublishedBefore() {
        return publishedBefore;
    }

    public static class Builder {
        private PostStatus status;

        private String category;

        private String displayName;

        private String text;

        private Boolean featured;

        private Boolean picked;

        private Boolean splash;

        private Instant modifiedAfter;

        private Instant modifiedBefore;

        private Instant publishedAfter;

        private Instant publishedBefore;

        public Builder(PostStatus status) {
            this.status = status;
        }

        public Builder(String status) {
            this.status = PostStatus.valueOf(status);
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder searchText(String text) {
            this.text = text;
            return this;
        }

        public Builder featured(boolean isFeatured) {
            this.featured = isFeatured;
            return this;
        }

        public Builder picked(boolean isPicked) {
            this.picked = isPicked;
            return this;
        }

        public Builder splash(boolean isSplash) {
            this.splash = isSplash;
            return this;
        }

        public Builder publishedAfter(Instant publishedAfter) {
            this.publishedAfter = publishedAfter;
            return this;
        }

        public Builder publishedBefore(Instant publishedBefore) {
            this.publishedBefore = publishedBefore;
            return this;
        }

        public Builder modifiedAfter(Instant modifiedAfter) {
            this.modifiedAfter = modifiedAfter;
            return this;
        }

        public Builder modifiedBefore(Instant modifiedBefore) {
            this.modifiedBefore = modifiedBefore;
            return this;
        }

        public PostQuery build() {
            PostQuery query = new PostQuery();

            // Required
            query.status = status;

            if (category != null) {
                query.category = category;
            }
            if (displayName != null) {
                query.displayName = displayName;
            }
            if (text != null) {
                query.text = text;
            }
            if (featured != null) {
                query.featured = featured;
            }
            if (picked != null) {
                query.picked = picked;
            }
            if (splash != null) {
                query.splash = splash;
            }
            if (publishedAfter != null) {
                query.publishedAfter = instantToLocalDateTime(publishedAfter);
            }
            if (publishedBefore != null) {
                query.publishedBefore = instantToLocalDateTime(publishedBefore);
            }
            if (modifiedAfter != null) {
                query.modifiedAfter = instantToLocalDateTime(modifiedAfter);
            }
            if (modifiedBefore != null) {
                query.modifiedBefore = instantToLocalDateTime(modifiedBefore);
            }
            return query;
        }
    }
}
