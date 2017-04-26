package com.indiepost.dto.stat;

import java.util.List;

/**
 * Created by jake on 17. 4. 27.
 */
public class SiteStats {
    private Long totalPageview;
    private Long totalVisitor;
    private Long totalAppVisitor;
    private float pageviewPerVisitor;
    private float postviewPerVisitor;
    private String period;
    private List<TimeDomainStatResult> pageviewTrend;
    private List<TimeDomainStatResult> visitorTrend;
    private List<ShareStatResult> pageviewByCategory;
    private List<ShareStatResult> pageviewByAuthor;
    private List<ShareStatResult> mostViewedPages;
    private List<ShareStatResult> mostViewedPosts;
    private List<ShareStatResult> secondlyViewedPages;
    private List<ShareStatResult> secondlyViewedPosts;
    private List<ShareStatResult> topReferrer;
    private List<ShareStatResult> topBrowser;
    private List<ShareStatResult> topOs;
    private List<ShareStatResult> topTags;
    private List<ShareStatResult> topChannel;

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

    public List<TimeDomainStatResult> getPageviewTrend() {
        return pageviewTrend;
    }

    public void setPageviewTrend(List<TimeDomainStatResult> pageviewTrend) {
        this.pageviewTrend = pageviewTrend;
    }

    public List<TimeDomainStatResult> getVisitorTrend() {
        return visitorTrend;
    }

    public void setVisitorTrend(List<TimeDomainStatResult> visitorTrend) {
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

    public List<ShareStatResult> getMostViewedPages() {
        return mostViewedPages;
    }

    public void setMostViewedPages(List<ShareStatResult> mostViewedPages) {
        this.mostViewedPages = mostViewedPages;
    }

    public List<ShareStatResult> getMostViewedPosts() {
        return mostViewedPosts;
    }

    public void setMostViewedPosts(List<ShareStatResult> mostViewedPosts) {
        this.mostViewedPosts = mostViewedPosts;
    }

    public List<ShareStatResult> getSecondlyViewedPages() {
        return secondlyViewedPages;
    }

    public void setSecondlyViewedPages(List<ShareStatResult> secondlyViewedPages) {
        this.secondlyViewedPages = secondlyViewedPages;
    }

    public List<ShareStatResult> getSecondlyViewedPosts() {
        return secondlyViewedPosts;
    }

    public void setSecondlyViewedPosts(List<ShareStatResult> secondlyViewedPosts) {
        this.secondlyViewedPosts = secondlyViewedPosts;
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
}
