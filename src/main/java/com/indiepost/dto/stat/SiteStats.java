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
    private List<PostStatDto> postsByPageview;
    private Trend pageviewTrend;
    private Trend visitorTrend;
    private List<ShareStat> pageviewByCategory;
    private List<ShareStat> pageviewByAuthor;
    private List<ShareStat> topPagesWebapp;
    private List<ShareStat> topPagesMobile;
    private List<ShareStat> topPosts;
    private List<ShareStat> topPostsMobile;
    private List<ShareStat> topPostsWebapp;
    private List<ShareStat> secondaryPagesMobile;
    private List<ShareStat> secondaryPagesWebapp;
    private List<ShareStat> secondaryPostsMobile;
    private List<ShareStat> secondaryPostsWebapp;
    private List<ShareStat> topLandingPagesMobile;
    private List<ShareStat> topLandingPagesWebapp;
    private List<ShareStat> topLandingPostsMobile;
    private List<ShareStat> topLandingPostsWebapp;
    private List<ShareStat> topReferrer;
    private List<ShareStat> topBrowser;
    private List<ShareStat> topOs;
    private List<ShareStat> topTags;
    private List<ShareStat> topChannel;

    public List<PostStatDto> getPostsByPageview() {
        return postsByPageview;
    }

    public void setPostsByPageview(List<PostStatDto> postsByPageview) {
        this.postsByPageview = postsByPageview;
    }

    public Long getTotalUniquePostview() {
        return totalUniquePostview;
    }

    public void setTotalUniquePostview(Long totalUniquePostview) {
        this.totalUniquePostview = totalUniquePostview;
    }

    public List<ShareStat> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<ShareStat> topPosts) {
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

    public List<ShareStat> getTopLandingPagesWebapp() {
        return topLandingPagesWebapp;
    }

    public void setTopLandingPagesWebapp(List<ShareStat> topLandingPagesWebapp) {
        this.topLandingPagesWebapp = topLandingPagesWebapp;
    }

    public List<ShareStat> getTopLandingPostsMobile() {
        return topLandingPostsMobile;
    }

    public void setTopLandingPostsMobile(List<ShareStat> topLandingPostsMobile) {
        this.topLandingPostsMobile = topLandingPostsMobile;
    }

    public List<ShareStat> getSecondaryPagesWebapp() {
        return secondaryPagesWebapp;
    }

    public void setSecondaryPagesWebapp(List<ShareStat> secondaryPagesWebapp) {
        this.secondaryPagesWebapp = secondaryPagesWebapp;
    }

    public List<ShareStat> getSecondaryPostsWebapp() {
        return secondaryPostsWebapp;
    }

    public void setSecondaryPostsWebapp(List<ShareStat> secondaryPostsWebapp) {
        this.secondaryPostsWebapp = secondaryPostsWebapp;
    }

    public List<ShareStat> getTopPostsMobile() {
        return topPostsMobile;
    }

    public void setTopPostsMobile(List<ShareStat> topPostsMobile) {
        this.topPostsMobile = topPostsMobile;
    }

    public List<ShareStat> getTopPagesMobile() {
        return topPagesMobile;
    }

    public void setTopPagesMobile(List<ShareStat> topPagesMobile) {
        this.topPagesMobile = topPagesMobile;
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

    public Trend getPageviewTrend() {
        return pageviewTrend;
    }

    public void setPageviewTrend(Trend pageviewTrend) {
        this.pageviewTrend = pageviewTrend;
    }

    public Trend getVisitorTrend() {
        return visitorTrend;
    }

    public void setVisitorTrend(Trend visitorTrend) {
        this.visitorTrend = visitorTrend;
    }

    public List<ShareStat> getPageviewByCategory() {
        return pageviewByCategory;
    }

    public void setPageviewByCategory(List<ShareStat> pageviewByCategory) {
        this.pageviewByCategory = pageviewByCategory;
    }

    public List<ShareStat> getPageviewByAuthor() {
        return pageviewByAuthor;
    }

    public void setPageviewByAuthor(List<ShareStat> pageviewByAuthor) {
        this.pageviewByAuthor = pageviewByAuthor;
    }

    public List<ShareStat> getTopPagesWebapp() {
        return topPagesWebapp;
    }

    public void setTopPagesWebapp(List<ShareStat> topPagesWebapp) {
        this.topPagesWebapp = topPagesWebapp;
    }

    public List<ShareStat> getTopPostsWebapp() {
        return topPostsWebapp;
    }

    public void setTopPostsWebapp(List<ShareStat> topPostsWebapp) {
        this.topPostsWebapp = topPostsWebapp;
    }

    public List<ShareStat> getSecondaryPagesMobile() {
        return secondaryPagesMobile;
    }

    public void setSecondaryPagesMobile(List<ShareStat> secondaryPagesMobile) {
        this.secondaryPagesMobile = secondaryPagesMobile;
    }

    public List<ShareStat> getSecondaryPostsMobile() {
        return secondaryPostsMobile;
    }

    public void setSecondaryPostsMobile(List<ShareStat> secondaryPostsMobile) {
        this.secondaryPostsMobile = secondaryPostsMobile;
    }

    public List<ShareStat> getTopReferrer() {
        return topReferrer;
    }

    public void setTopReferrer(List<ShareStat> topReferrer) {
        this.topReferrer = topReferrer;
    }

    public List<ShareStat> getTopBrowser() {
        return topBrowser;
    }

    public void setTopBrowser(List<ShareStat> topBrowser) {
        this.topBrowser = topBrowser;
    }

    public List<ShareStat> getTopOs() {
        return topOs;
    }

    public void setTopOs(List<ShareStat> topOs) {
        this.topOs = topOs;
    }

    public List<ShareStat> getTopTags() {
        return topTags;
    }

    public void setTopTags(List<ShareStat> topTags) {
        this.topTags = topTags;
    }

    public List<ShareStat> getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(List<ShareStat> topChannel) {
        this.topChannel = topChannel;
    }

    public List<ShareStat> getTopLandingPagesMobile() {
        return topLandingPagesMobile;
    }

    public void setTopLandingPagesMobile(List<ShareStat> topLandingPagesMobile) {
        this.topLandingPagesMobile = topLandingPagesMobile;
    }

    public List<ShareStat> getTopLandingPostsWebapp() {
        return topLandingPostsWebapp;
    }

    public void setTopLandingPostsWebapp(List<ShareStat> topLandingPostsWebapp) {
        this.topLandingPostsWebapp = topLandingPostsWebapp;
    }
}
