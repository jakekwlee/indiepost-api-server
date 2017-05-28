package com.indiepost.dto.stat;

/**
 * Created by jake on 17. 4. 26.
 */
public class ShareStat {
    private String statName;

    private Long statValue;

    public ShareStat() {
    }

    public ShareStat(String statName, Long statValue) {
        this.statName = statName;
        this.statValue = statValue;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public Long getStatValue() {
        return statValue;
    }

    public void setStatValue(Long statValue) {
        this.statValue = statValue;
    }
}
