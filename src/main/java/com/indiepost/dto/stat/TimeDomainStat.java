package com.indiepost.dto.stat;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStat {

    private Date statDateTime;

    private BigInteger statCount;

    public TimeDomainStat() {
    }

    public TimeDomainStat(Date statDateTime, BigInteger statCount) {
        this.statDateTime = statDateTime;
        this.statCount = statCount;
    }

    public Date getStatDateTime() {
        return statDateTime;
    }

    public void setStatDateTime(Date statDateTime) {
        this.statDateTime = statDateTime;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
