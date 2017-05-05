package com.indiepost.dto.stat;

import java.util.List;

/**
 * Created by jake on 17. 4. 27.
 */
public class SiteStats {
    private Long totalPageview;
    private Long totalUniquePageview;
    private Long totalUniquePostview;
    private Long totalPostview;
    private Long totalVisitor;
    private Long totalAppVisitor;
    private float pageviewPerVisitor;
    private float postviewPerVisitor;
    private String period;
    private List<TimeDomainStat> pageviewTrend;
    private List<TimeDomainStat> visitorTrend;
    private List<ShareStatResult> pageviewByCategory;
    private List<ShareStatResult> pageviewByAuthor;
    private List<ShareStatResult> topPagesWebapp;
    private List<ShareStatResult> topPagesMobile;
    private List<ShareStatResult> topPosts;
    private List<ShareStatResult> topPostsMobile;
    private List<ShareStatResult> topPostsWebapp;
    private List<ShareStatResult> secondaryPagesMobile;
    private List<ShareStatResult> secondaryPagesWebapp;
    private List<ShareStatResult> secondaryPostsMobile;
    private List<ShareStatResult> secondaryPostsWebapp;
    private List<ShareStatResult> topLandingPagesMobile;
    private List<ShareStatResult> topLandingPagesWebapp;
    private List<ShareStatResult> topLandingPostsMobile;
    private List<ShareStatResult> topLandingPostsWebapp;
    private List<ShareStatResult> topReferrer;
    private List<ShareStatResult> topBrowser;
    private List<ShareStatResult> topOs;
    private List<ShareStatResult> topTags;
    private List<ShareStatResult> topChannel;

    public Long getTotalUniquePostview() {
        return totalUniquePostview;
    }

    public void setTotalUniquePostview(Long totalUniquePostview) {
        this.totalUniquePostview = totalUniquePostview;
    }

    public List<ShareStatResult> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<ShareStatResult> topPosts) {
        this.topPosts = topPosts;
    }

    public Long getTotalPostview() {
        return totalPostview;
    }

    public void setTotalPostview(Long totalPostview) {
        this.totalPostview = totalPostview;
    }

    public Long getTotalUniquePageview() {
        return totalUniquePageview;
    }

    public void setTotalUniquePageview(Long totalUniquePageview) {
        this.totalUniquePageview = totalUniquePageview;
    }

    public List<ShareStatResult> getTopLandingPagesWebapp() {
        return topLandingPagesWebapp;
    }

    public void setTopLandingPagesWebapp(List<ShareStatResult> topLandingPagesWebapp) {
        this.topLandingPagesWebapp = topLandingPagesWebapp;
    }

    public List<ShareStatResult> getTopLandingPostsMobile() {
        return topLandingPostsMobile;
    }

    public void setTopLandingPostsMobile(List<ShareStatResult> topLandingPostsMobile) {
        this.topLandingPostsMobile = topLandingPostsMobile;
    }

    public List<ShareStatResult> getSecondaryPagesWebapp() {
        return secondaryPagesWebapp;
    }

    public void setSecondaryPagesWebapp(List<ShareStatResult> secondaryPagesWebapp) {
        this.secondaryPagesWebapp = secondaryPagesWebapp;
    }

    public List<ShareStatResult> getSecondaryPostsWebapp() {
        return secondaryPostsWebapp;
    }

    public void setSecondaryPostsWebapp(List<ShareStatResult> secondaryPostsWebapp) {
        this.secondaryPostsWebapp = secondaryPostsWebapp;
    }

    public List<ShareStatResult> getTopPostsMobile() {
        return topPostsMobile;
    }

    public void setTopPostsMobile(List<ShareStatResult> topPostsMobile) {
        this.topPostsMobile = topPostsMobile;
    }

    public List<ShareStatResult> getTopPagesMobile() {
        return topPagesMobile;
    }

    public void setTopPagesMobile(List<ShareStatResult> topPagesMobile) {
        this.topPagesMobile = topPagesMobile;
    }

    public float getPageviewPerVisitor() {
        return pageviewPerVisitor;
    }

    public void setPageviewPerVisitor(float pageviewPerVisitor) {
        this.pageviewPerVisitor = pageviewPerVisitor;
    }

    public float getPostviewPerVisitor() {
        return postviewPerVisitor;
    }

    public void setPostviewPerVisitor(float postviewPerVisitor) {
        this.postviewPerVisitor = postviewPerVisitor;
    }

    public Long getTotalPageview() {
        return totalPageview;
    }

    public void setTotalPageview(Long totalPageview) {
        this.totalPageview = totalPageview;
    }

    public Long getTotalVisitor() {
        return totalVisitor;
    }

    public void setTotalVisitor(Long totalVisitor) {
        this.totalVisitor = totalVisitor;
    }

    public Long getTotalAppVisitor() {
        return totalAppVisitor;
    }

    public void setTotalAppVisitor(Long totalAppVisitor) {
        this.totalAppVisitor = totalAppVisitor;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<TimeDomainStat> getPageviewTrend() {
        return pageviewTrend;
    }

    public void setPageviewTrend(List<TimeDomainStat> pageviewTrend) {
        this.pageviewTrend = pageviewTrend;
    }

    public List<TimeDomainStat> getVisitorTrend() {
        return visitorTrend;
    }

    public void setVisitorTrend(List<TimeDomainStat> visitorTrend) {
        this.visitorTrend = visitorTrend;
    }

    public List<ShareStatResult> getPageviewByCategory() {
        return pageviewByCategory;
    }

    public void setPageviewByCategory(List<ShareStatResult> pageviewByCategory) {
        this.pageviewByCategory = pageviewByCategory;
    }

    public List<ShareStatResult> getPageviewByAuthor() {
        return pageviewByAuthor;
    }

    public void setPageviewByAuthor(List<ShareStatResult> pageviewByAuthor) {
        this.pageviewByAuthor = pageviewByAuthor;
    }

    public List<ShareStatResult> getTopPagesWebapp() {
        return topPagesWebapp;
    }

    public void setTopPagesWebapp(List<ShareStatResult> topPagesWebapp) {
        this.topPagesWebapp = topPagesWebapp;
    }

    public List<ShareStatResult> getTopPostsWebapp() {
        return topPostsWebapp;
    }

    public void setTopPostsWebapp(List<ShareStatResult> topPostsWebapp) {
        this.topPostsWebapp = topPostsWebapp;
    }

    public List<ShareStatResult> getSecondaryPagesMobile() {
        return secondaryPagesMobile;
    }

    public void setSecondaryPagesMobile(List<ShareStatResult> secondaryPagesMobile) {
        this.secondaryPagesMobile = secondaryPagesMobile;
    }

    public List<ShareStatResult> getSecondaryPostsMobile() {
        return secondaryPostsMobile;
    }

    public void setSecondaryPostsMobile(List<ShareStatResult> secondaryPostsMobile) {
        this.secondaryPostsMobile = secondaryPostsMobile;
    }

    public List<ShareStatResult> getTopReferrer() {
        return topReferrer;
    }

    public void setTopReferrer(List<ShareStatResult> topReferrer) {
        this.topReferrer = topReferrer;
    }

    public List<ShareStatResult> getTopBrowser() {
        return topBrowser;
    }

    public void setTopBrowser(List<ShareStatResult> topBrowser) {
        this.topBrowser = topBrowser;
    }

    public List<ShareStatResult> getTopOs() {
        return topOs;
    }

    public void setTopOs(List<ShareStatResult> topOs) {
        this.topOs = topOs;
    }

    public List<ShareStatResult> getTopTags() {
        return topTags;
    }

    public void setTopTags(List<ShareStatResult> topTags) {
        this.topTags = topTags;
    }

    public List<ShareStatResult> getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(List<ShareStatResult> topChannel) {
        this.topChannel = topChannel;
    }

    public List<ShareStatResult> getTopLandingPagesMobile() {
        return topLandingPagesMobile;
    }

    public void setTopLandingPagesMobile(List<ShareStatResult> topLandingPagesMobile) {
        this.topLandingPagesMobile = topLandingPagesMobile;
    }

    public List<ShareStatResult> getTopLandingPostsWebapp() {
        return topLandingPostsWebapp;
    }

    public void setTopLandingPostsWebapp(List<ShareStatResult> topLandingPostsWebapp) {
        this.topLandingPostsWebapp = topLandingPostsWebapp;
    }
}
