package com.indiepost.dto.stat;

import java.util.List;

public class CampaignReport {
    private CampaignDto campaign;

    private List<TimeDomainStat> trend;

    private List<ShareStat> byOS;

    private List<ShareStat> byLinks;

    private List<ShareStat> byBrowsers;

    private List<ShareStat> topPrevious;

    private List<RawDataReportRow> rawData;

    public CampaignDto getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignDto campaign) {
        this.campaign = campaign;
    }

    public List<TimeDomainStat> getTrend() {
        return trend;
    }

    public void setTrend(List<TimeDomainStat> trend) {
        this.trend = trend;
    }

    public List<ShareStat> getByOS() {
        return byOS;
    }

    public void setByOS(List<ShareStat> byOS) {
        this.byOS = byOS;
    }

    public List<ShareStat> getByLinks() {
        return byLinks;
    }

    public void setByLinks(List<ShareStat> byLinks) {
        this.byLinks = byLinks;
    }

    public List<ShareStat> getByBrowsers() {
        return byBrowsers;
    }

    public void setByBrowsers(List<ShareStat> byBrowsers) {
        this.byBrowsers = byBrowsers;
    }

    public List<ShareStat> getTopPrevious() {
        return topPrevious;
    }

    public void setTopPrevious(List<ShareStat> topPrevious) {
        this.topPrevious = topPrevious;
    }

    public List<RawDataReportRow> getRawData() {
        return rawData;
    }

    public void setRawData(List<RawDataReportRow> rawData) {
        this.rawData = rawData;
    }
}
