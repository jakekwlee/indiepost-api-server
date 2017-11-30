package com.indiepost.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Posts_Contributors")
public class PostContributor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contributor_id")
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
        int result = post.hashCode();
        result = 31 * result + contributor.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostContributor that = (PostContributor) o;

        if (!post.equals(that.post)) return false;
        return contributor.equals(that.contributor);
    }
}
