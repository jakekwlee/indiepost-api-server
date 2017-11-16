package com.indiepost.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Posts_Tags_New")
public class PostTag {
    @EmbeddedId
    private PostTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tagId")
    private Tag tag;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int priority;

    private PostTag() {
    }

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public PostTag(Post post, Tag tag, LocalDateTime createdAt, int priority) {
        this.post = post;
        this.tag = tag;
        this.createdAt = createdAt;
        this.priority = priority;
    }

    public PostTagId getId() {
        return id;
    }

    public void setId(PostTagId id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostTag that = (PostTag) obj;
        return Objects.equals(post, that.post) &&
                Objects.equals(tag, that.tag);
    }
}
