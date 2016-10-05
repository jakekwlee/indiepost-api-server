package com.indiepost.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 7/25/16.
 */
//@Entity
//@Table(name = "Subscriptions")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private User author;

    private Date subscriptedAt;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSubscriptedAt() {
        return subscriptedAt;
    }

    public void setSubscriptedAt(Date subscriptedAt) {
        this.subscriptedAt = subscriptedAt;
    }
}
