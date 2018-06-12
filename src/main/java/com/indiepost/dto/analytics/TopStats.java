package com.indiepost.dto.analytics;

import java.util.List;

public class TopStats {
    private PeriodDto period;

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

    public PeriodDto getPeriod() {
        return period;
    }

    public void setPeriod(PeriodDto period) {
        this.period = period;
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

    public List<ShareStat> getTopPagesMobile() {
        return topPagesMobile;
    }

    public void setTopPagesMobile(List<ShareStat> topPagesMobile) {
        this.topPagesMobile = topPagesMobile;
    }

    public List<ShareStat> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<ShareStat> topPosts) {
        this.topPosts = topPosts;
    }

    public List<ShareStat> getTopPostsMobile() {
        return topPostsMobile;
    }

    public void setTopPostsMobile(List<ShareStat> topPostsMobile) {
        this.topPostsMobile = topPostsMobile;
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
