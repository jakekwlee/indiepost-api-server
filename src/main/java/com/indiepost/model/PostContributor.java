package com.indiepost.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Posts_Contributors")
public class PostContributor {
    @EmbeddedId
    private PostContributorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("contributorId")
    private Contributor contributor;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int priority;

    private PostContributor() {
    }

    public PostContributor(Post post, Contributor contributor) {
        this.post = post;
        this.contributor = contributor;
    }

    public PostContributor(Post post, Contributor contributor, LocalDateTime createdAt, int priority) {
        this.post = post;
        this.contributor = contributor;
        this.createdAt = createdAt;
        this.priority = priority;
    }

    public PostContributorId getId() {
        return id;
    }

    public void setId(PostContributorId id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
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
        return Objects.hash(post, contributor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostContributor that = (PostContributor) obj;
        return Objects.equals(post, that.post) &&
                Objects.equals(contributor, that.contributor);
    }
}
