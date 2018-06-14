package com.indiepost.model;

import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.enums.Types.UserState;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = -1147949899202777107L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 200)
    private String username;

    @Size(max = 500)
    private String profile;

    @Size(max = 500)
    private String picture;

    @Column(unique = true)
    @Size(min = 7, max = 50)
    private String email;

    @Pattern(regexp = "[_0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣\\s]{2,30}")
    @Size(min = 2, max = 30)
    private String displayName;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Pattern(regexp = "^01[\\d]{8,9}")
    @Size(min = 7, max = 15)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state = UserState.ACTIVATED;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Bookmark> bookmarks;

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Users_Roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles = new ArrayList<>();

    public UserRole getHighestRole() {
        int userLevel = 1;
        for (Role role : this.roles) {
            if (role.getLevel() > userLevel) {
                userLevel = role.getLevel();
            }
        }
        switch (userLevel) {
            case 9:
                return UserRole.Administrator;
            case 7:
                return UserRole.EditorInChief;
            case 5:
                return UserRole.Editor;
            case 3:
                return UserRole.Author;
            case 1:
                return UserRole.User;
            default:
                return UserRole.User;
        }
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
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

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public boolean hasRole(UserRole roles) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
