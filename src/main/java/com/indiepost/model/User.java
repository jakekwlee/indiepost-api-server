package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(unique = true)
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Column(unique = true)
    @Size(min = 7, max = 50)
    @Email
    private String email;

    @Pattern(regexp = "[_0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}")
    @Size(min = 2, max = 20)
    private String displayName;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt;

    @Pattern(regexp = "^01[\\d]{8,9}")
    @Size(min = 7, max = 15)
    private String phone;

    @Size(max = 100)
    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @NotNull
    @Enumerated(EnumType.STRING)
    private State state;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Subscription> subscriptions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Bookmark> bookmarkes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Like> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private Set<Post> postsAuthoredByMe;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "editor")
    private Set<Post> postsEditedByMe;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Users_Roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles;

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Bookmark> getBookmarkes() {
        return bookmarkes;
    }

    public void setBookmarkes(Set<Bookmark> bookmarkes) {
        this.bookmarkes = bookmarkes;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Post> getPostsAuthoredByMe() {
        return postsAuthoredByMe;
    }

    public void setPostsAuthoredByMe(Set<Post> postsAuthoredByMe) {
        this.postsAuthoredByMe = postsAuthoredByMe;
    }

    public Set<Post> getPostsEditedByMe() {
        return postsEditedByMe;
    }

    public void setPostsEditedByMe(Set<Post> postsEditedByMe) {
        this.postsEditedByMe = postsEditedByMe;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public enum State {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }

    public enum Gender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    public enum Roles {
        User, Author, Editor, EditorInChief, Administrator
    }
}
