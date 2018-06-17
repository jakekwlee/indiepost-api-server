package com.indiepost.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "UserReadings")
public class UserReading implements Serializable {

    private static final long serialVersionUID = 5793341286387473902L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "post_id", insertable = false, updatable = false)
    private Long postId;

    @Column(nullable = false)
    private Instant lastRead;

    private boolean bookmarked;

    private boolean visible = true;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Instant getLastRead() {
        return lastRead;
    }

    public void setLastRead(Instant lastRead) {
        this.lastRead = lastRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}