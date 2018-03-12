package com.indiepost.model;

import com.indiepost.enums.Types.ContributorDisplayType;
import com.indiepost.enums.Types.ContributorType;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Contributors")
public class Contributor {

    private static final long serialVersionUID = 7763721241430441486L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(
            mappedBy = "contributor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("publishedAt desc")
    private List<PostContributor> postContributors = new ArrayList<>();

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(max = 30)
    private String name;

    @Size(max = 50)
    private String email = "email@example.com";

    @Size(max = 50)
    private String subEmail;

    @Size(max = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @URL
    @Size(max = 500)
    private String url;

    private String phone;

    @URL
    @Size(max = 500)
    private String picture;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorDisplayType displayType;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorType contributorType;

    private String etc;

    private boolean titleVisible = false;

    private boolean emailVisible = false;

    private boolean descriptionVisible = false;

    private boolean urlVisible = false;

    private boolean pictureVisible = false;

    private boolean phoneVisible = false;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    public List<PostContributor> getPostContributors() {
        return postContributors;
    }

    public void setPostContributors(List<PostContributor> postContributors) {
        this.postContributors = postContributors;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public ContributorDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(ContributorDisplayType displayType) {
        this.displayType = displayType;
    }

    public ContributorType getContributorType() {
        return contributorType;
    }

    public void setContributorType(ContributorType contributorType) {
        this.contributorType = contributorType;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
