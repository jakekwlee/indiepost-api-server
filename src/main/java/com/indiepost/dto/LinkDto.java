package com.indiepost.dto;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by jake on 8/10/17.
 */
public class LinkDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @Min(2)
    @Max(30)
    private String name;

    @NotNull
    @URL
    private String url;

    @NotNull
    private Long campaignId;

    private String uid;

    private Long allClicks;

    private Long validClicks;

    private LocalDateTime createdAt;

    public Long getValidClicks() {
        return validClicks;
    }

    public void setValidClicks(Long validClicks) {
        this.validClicks = validClicks;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getAllClicks() {
        return allClicks;
    }

    public void setAllClicks(Long allClicks) {
        this.allClicks = allClicks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
