package com.indiepost.dto.stat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.utils.LocalDateTimeSerializerCustom;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStat {

    @JsonSerialize(using = LocalDateTimeSerializerCustom.class)
    private LocalDateTime statDatetime;

    private BigInteger statCount;

    public TimeDomainStat() {
    }

    public TimeDomainStat(LocalDateTime statDatetime, BigInteger statCount) {
        this.statDatetime = statDatetime;
        this.statCount = statCount;
    }

    public LocalDateTime getStatDatetime() {
        return statDatetime;
    }

    public void setStatDatetime(LocalDateTime statDatetime) {
        this.statDatetime = statDatetime;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
