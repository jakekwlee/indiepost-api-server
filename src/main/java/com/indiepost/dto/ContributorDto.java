package com.indiepost.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;

public class ContributorDto {
    private Long id;

    private String name;

    private String email;

    private String subEmail;

    private String title;

    private String description;

    private String url;

    private String phone;

    private String picture;

    private String displayType;

    private String contributorType;

    private String etc;

    private int priority;

    private boolean titleVisible;

    private boolean emailVisible;

    private boolean descriptionVisible;

    private boolean urlVisible;

    private boolean pictureVisible;

    private boolean phoneVisible;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime lastUpdated;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime created;

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isTitleVisible() {
        return titleVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    public boolean isEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(boolean emailVisible) {
        this.emailVisible = emailVisible;
    }

    public boolean isDescriptionVisible() {
        return descriptionVisible;
    }

    public void setDescriptionVisible(boolean descriptionVisible) {
        this.descriptionVisible = descriptionVisible;
    }

    public boolean isUrlVisible() {
        return urlVisible;
    }

    public void setUrlVisible(boolean urlVisible) {
        this.urlVisible = urlVisible;
    }

    public boolean isPictureVisible() {
        return pictureVisible;
    }

    public void setPictureVisible(boolean pictureVisible) {
        this.pictureVisible = pictureVisible;
    }

    public boolean isPhoneVisible() {
        return phoneVisible;
    }

    public void setPhoneVisible(boolean phoneVisible) {
        this.phoneVisible = phoneVisible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubEmail() {
        return subEmail;
    }

    public void setSubEmail(String subEmail) {
        this.subEmail = subEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getContributorType() {
        return contributorType;
    }

    public void setContributorType(String contributorType) {
        this.contributorType = contributorType;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
