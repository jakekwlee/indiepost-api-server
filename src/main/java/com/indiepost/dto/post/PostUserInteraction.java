package com.indiepost.dto.post;

import java.time.Instant;

public class PostUserInteraction {

    private final Long postId;

    private Instant lastRead;

    private Instant bookmarked;

    public PostUserInteraction(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
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
}
