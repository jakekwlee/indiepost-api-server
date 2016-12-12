package com.indiepost.model.response;

import com.indiepost.enums.UserEnum;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public class AdminUserResponse {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String displayName;

    private String email;

    private Date joinedAt;

    private Date birthday;

    private String profile;

    private String picture;

    private UserEnum.Gender gender;

    private List<Long> roleList;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public UserEnum.Gender getGender() {
        return gender;
    }

    public void setGender(UserEnum.Gender gender) {
        this.gender = gender;
    }

    public List<Long> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Long> roleList) {
        this.roleList = roleList;
    }
}
