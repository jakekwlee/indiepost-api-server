package com.indiepost.dto.stat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStat {

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
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
