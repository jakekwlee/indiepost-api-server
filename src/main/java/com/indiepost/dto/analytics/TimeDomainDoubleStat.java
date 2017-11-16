package com.indiepost.dto.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;

/**
 * Created by jake on 10/29/17.
 */
public class TimeDomainDoubleStat {

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime dateTime;

    private Long value1;

    private Long value2;

    public TimeDomainDoubleStat(LocalDateTime dateTime, long value1, long value2) {
        this.dateTime = dateTime;
        this.value1 = value1;
        this.value2 = value2;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getValue1() {
        return value1;
    }

    public void setValue1(Long value1) {
        this.value1 = value1;
    }

    public Long getValue2() {
        return value2;
    }

    public void setValue2(Long value2) {
        this.value2 = value2;
    }
}
