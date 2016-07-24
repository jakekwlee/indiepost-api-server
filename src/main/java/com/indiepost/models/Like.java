package com.indiepost.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "likes")
public class Like {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    Date likedAt;

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
