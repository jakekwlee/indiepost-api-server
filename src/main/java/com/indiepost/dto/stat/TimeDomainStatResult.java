package com.indiepost.dto.stat;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jake on 17. 4. 24.
 */
public class TimeDomainStatResult {
    private Date statDate;

    private BigInteger statCount;

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
