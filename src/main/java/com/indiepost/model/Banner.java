package com.indiepost.model;

import com.indiepost.enums.Types.BannerTarget;
import com.indiepost.enums.Types.BannerType;
import com.indiepost.model.analytics.Link;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Banners")
public class Banner {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 30)
    private String title;

    @Size(max = 30)
    private String subtitle;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;

    @Column(nullable = false)
    @Size(max = 12)
    private String bgColor = "#ccc";

    @Size(max = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean cover;

    private String internalUrl;

    @Column(nullable = false)
    private int priority;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "linkId")
    private Link link;

    @Column(name = "linkId", insertable = false, updatable = false)
    private Long linkId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BannerTarget target = BannerTarget.All;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BannerType getBannerType() {
        return bannerType;
    }

    public void setBannerType(BannerType bannerType) {
        this.bannerType = bannerType;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }

    public BannerTarget getTarget() {
        return target;
    }

    public void setTarget(BannerTarget target) {
        this.target = target;
    }
}
