package com.indiepost.dto.analytics;

/**
 * Created by jake on 17. 4. 27.
 */
public class OverviewStats {

    private Long totalPageview;

    private Long totalUniquePageview;

    private Long totalUniquePostview;

    private Long totalPostview;

    private Long totalVisitor;

    private Long totalAppVisitor;

    private Trend pageviewTrend;

    private Trend visitorTrend;

    private PeriodDto period;

    public Long getTotalUniquePostview() {
        return totalUniquePostview;
    }

    public void setTotalUniquePostview(Long totalUniquePostview) {
        this.totalUniquePostview = totalUniquePostview;
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

    public PeriodDto getPeriod() {
        return period;
    }

    public void setPeriod(PeriodDto period) {
        this.period = period;
    }
}
