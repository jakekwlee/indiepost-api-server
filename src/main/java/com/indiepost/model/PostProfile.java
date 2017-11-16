package com.indiepost.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Posts_Profiles")
public class PostProfile {
    @EmbeddedId
    private PostProfileId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("profileId")
    private Profile profile;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int priority;

    private PostProfile() {
    }

    public PostProfile(Post post, Profile profile) {
        this.post = post;
        this.profile = profile;
    }

    public PostProfile(Post post, Profile profile, LocalDateTime createdAt, int priority) {
        this.post = post;
        this.profile = profile;
        this.createdAt = createdAt;
        this.priority = priority;
    }

    public PostProfileId getId() {
        return id;
    }

    public void setId(PostProfileId id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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
        return Objects.hash(post, profile);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostProfile that = (PostProfile) obj;
        return Objects.equals(post, that.post) &&
                Objects.equals(profile, that.profile);
    }
}
