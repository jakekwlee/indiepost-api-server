package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.indiepost.enums.UserEnum;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Size(min = 4, max = 30)
    private String username;

    @NotNull
    @Size(min = 3, max = 50)
    private String password;

    @Size(max = 300)
    private String profile;

    private String picture;

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
    private Date joinedAt;

    @Pattern(regexp = "^01[\\d]{8,9}")
    @Size(min = 7, max = 15)
    private String phone;

    @Size(max = 100)
    private String uuid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserEnum.State state;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserEnum.Gender gender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Post> authoredPosts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "editor")
    private List<Post> editedPosts;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Users_Roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles;

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
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

    public UserEnum.State getState() {
        return state;
    }

    public void setState(UserEnum.State state) {
        this.state = state;
    }

    public List<Post> getAuthoredPosts() {
        return authoredPosts;
    }

    public void setAuthoredPosts(List<Post> authoredPosts) {
        this.authoredPosts = authoredPosts;
    }

    public List<Post> getEditedPosts() {
        return editedPosts;
    }

    public void setEditedPosts(List<Post> editedPosts) {
        this.editedPosts = editedPosts;
    }

    public UserEnum.Gender getGender() {
        return gender;
    }

    public void setGender(UserEnum.Gender gender) {
        this.gender = gender;
    }

    public boolean hasRole(UserEnum.Roles roles) {
        return getRoles().contains(roles);
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
