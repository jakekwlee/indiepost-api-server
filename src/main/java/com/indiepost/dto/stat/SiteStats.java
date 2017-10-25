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
    private Trend pageviewTrend;
    private Trend visitorTrend;
    private List<ShareStat> pageviewByCategory;
    private List<ShareStat> pageviewByAuthor;
    private List<ShareStat> topPagesWebapp;
    private List<ShareStat> topPagesMobile;
    private List<ShareStat> topPosts;
    private List<ShareStat> topPostsMobile;
    private List<ShareStat> topPostsWebapp;
    private List<ShareStat> topReferrer;
    private List<ShareStat> topBrowser;
    private List<ShareStat> topOs;
    private List<ShareStat> topTags;
    private List<ShareStat> topChannel;

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

}
