package com.indiepost.dto.analytics;

import java.util.List;

/**
 * Created by jake on 10/29/17.
 */
public class RecentAndOldPostStats {

    private DoubleTrend trend;

    private List<ShareStat> topOldPosts;

    private List<ShareStat> topRecentPosts;

    public DoubleTrend getTrend() {
        return trend;
    }

    public void setTrend(DoubleTrend trend) {
        this.trend = trend;
    }

    public List<ShareStat> getTopOldPosts() {
        return topOldPosts;
    }

    public void setTopOldPosts(List<ShareStat> topOldPosts) {
        this.topOldPosts = topOldPosts;
    }

    public List<ShareStat> getTopRecentPosts() {
        return topRecentPosts;
    }

    public void setTopRecentPosts(List<ShareStat> topRecentPosts) {
        this.topRecentPosts = topRecentPosts;
    }
}
