package com.indiepost.dto;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jake on 17. 4. 24.
 */
public class StatResult {
    private Date statDatetime;

    private BigInteger statCount;

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
