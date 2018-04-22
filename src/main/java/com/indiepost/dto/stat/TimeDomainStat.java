package com.indiepost.dto.stat;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStat {

    private LocalDateTime statDateTime;

    private Long statValue;

    public TimeDomainStat() {
    }

    public TimeDomainStat(LocalDateTime statDateTime, Long statValue) {
        this.statDateTime = statDateTime;
        this.statValue = statValue;
    }

    public LocalDateTime getStatDateTime() {
        return statDateTime;
    }

    public void setStatDateTime(LocalDateTime statDateTime) {
        this.statDateTime = statDateTime;
    }

    public Long getStatValue() {
        return statValue;
    }

    public void setStatValue(Long statValue) {
        this.statValue = statValue;
    }
}
