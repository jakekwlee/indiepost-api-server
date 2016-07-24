package com.indiepost.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(unique = true)
    @Size(min = 4, max = 20)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Column(unique = true)
    @Size(min = 7, max = 100)
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "[_0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}")
    private String displayName;

    @NotNull
    @Pattern(regexp = "^01[\\d]{8,9}")
    private String phone;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private String registeredAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("subscriptedAt DESC")
    private Set<Subscription> subscriptions;

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private enum Status {
        PENDING, APPROVED, EXPIRED, BANNED
    }
}
