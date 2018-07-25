package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Posts_Posts")
public class PostPost {
    private static final long serialVersionUID = 2610937958697175939L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relatedPostId")
    private Post relatedPost;

    @Column(name = "postId", updatable = false, insertable = false)
    private Long postId;

    @Column(name = "relatedPostId", updatable = false, insertable = false)
    private Long relatedPostId;

    @NotNull
    @Column(nullable = false)
    private int priority = 0;

    public PostPost(Post post, Post relatedPost, @NotNull int priority) {
        this.post = post;
        this.relatedPost = relatedPost;
        this.priority = priority;
    }

    public PostPost() {

    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getRelatedPostId() {
        return relatedPostId;
    }

    public void setRelatedPostId(Long relatedPostId) {
        this.relatedPostId = relatedPostId;
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

    public Post getRelatedPost() {
        return relatedPost;
    }

    public void setRelatedPost(Post relatedPost) {
        this.relatedPost = relatedPost;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
