package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Posts_Contributors")
public class PostContributor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "contributorId")
    private Contributor contributor;

    @NotNull
    @Column(nullable = false)
    private int priority = 0;

    public PostContributor(Post post, Contributor contributor) {
        this.post = post;
        this.contributor = contributor;
    }

    public PostContributor(Post post, Contributor contributor, int priority) {
        this.post = post;
        this.contributor = contributor;
        this.priority = priority;
    }

    public PostContributor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
