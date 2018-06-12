package com.indiepost.dto.analytics;

import com.indiepost.enums.Types.LinkType;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by jake on 8/10/17.
 */
public class LinkDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @URL
    private String url;

    @NotNull
    private Long campaignId;

    @NotNull
    @Size(min = 8, max = 30)
    private String uid;

    @NotNull
    private String linkType = LinkType.Standard.toString();

    private BannerDto banner;

    private Long allClicks;

    private Long validClicks;

    private LocalDateTime createdAt;

    private boolean isUpdated;

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public BannerDto getBanner() {
        return banner;
    }

    public void setBanner(BannerDto banner) {
        this.banner = banner;
    }

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

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
