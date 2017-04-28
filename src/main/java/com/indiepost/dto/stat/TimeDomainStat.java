package com.indiepost.dto.stat;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStat {
    private Date statDatetime;

    private BigInteger statCount;

    public TimeDomainStat() {
    }

    public TimeDomainStat(Date statDatetime, BigInteger statCount) {
        this.statDatetime = statDatetime;
        this.statCount = statCount;
    }

    public Date getStatDatetime() {
        return statDatetime;
    }

    public void setStatDatetime(Date statDatetime) {
        this.statDatetime = statDatetime;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
