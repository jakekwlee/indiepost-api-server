package com.indiepost.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "bookmarks")
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookmarkedAt;

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

    public Date getBookmarkedAt() {
        return bookmarkedAt;
    }

    public void setBookmarkedAt(Date bookmarkedAt) {
        this.bookmarkedAt = bookmarkedAt;
    }
}
