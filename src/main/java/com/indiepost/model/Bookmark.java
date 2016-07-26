package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "Bookmarks")
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postId")
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
