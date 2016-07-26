package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "Likes")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date likedAt;

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

    public Date getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Date likedAt) {
        this.likedAt = likedAt;
    }
}