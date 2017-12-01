package com.indiepost.model;

import com.indiepost.enums.Types.ContributorRole;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 11. 13.
 */
@Entity
@Table(name = "Contributors")
public class Contributor implements Serializable {

    private static final long serialVersionUID = 7763721241430441486L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "contributor",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("id desc")
    private List<PostContributor> postContributors = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @Email
    private String email;

    @Email
    private String subEmail;

    @Pattern(regexp = "^01[\\d]{8,9}")
    @Size(min = 7, max = 15)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(columnDefinition = "TEXT")
    private String description;

    @URL
    private String picture;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

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

    public ContributorRole getRole() {
        return role;
    }

    public void setRole(ContributorRole role) {
        this.role = role;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public List<PostContributor> getPostContributors() {
        return postContributors;
    }

    public void setPostContributors(List<PostContributor> postContributors) {
        this.postContributors = postContributors;
    }
}
