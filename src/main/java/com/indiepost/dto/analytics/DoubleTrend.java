package com.indiepost.dto.analytics;

import com.indiepost.enums.Types.TimeDomainDuration;

import java.util.List;

/**
 * Created by jake on 10/29/17.
 */
public class DoubleTrend {
    private String statName;

    private TimeDomainDuration duration;

    private List<TimeDomainDoubleStat> result;

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public TimeDomainDuration getDuration() {
        return duration;
    }

    public void setDuration(TimeDomainDuration duration) {
        this.duration = duration;
    }

    public List<TimeDomainDoubleStat> getResult() {
        return result;
    }

    public void setResult(List<TimeDomainDoubleStat> result) {
        this.result = result;
    }
}
