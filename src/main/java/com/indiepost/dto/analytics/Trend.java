package com.indiepost.dto.analytics;

import com.indiepost.enums.Types.TimeDomainDuration;

import java.util.List;

/**
 * Created by jake on 17. 5. 24.
 */
public class Trend {

    private TimeDomainDuration duration;

    private List<TimeDomainStat> result;

    public TimeDomainDuration getDuration() {
        return duration;
    }

    public void setDuration(TimeDomainDuration duration) {
        this.duration = duration;
    }

    public List<TimeDomainStat> getResult() {
        return result;
    }

    public void setResult(List<TimeDomainStat> result) {
        this.result = result;
    }
}
