package com.indiepost.dto.stat;

import java.math.BigInteger;

/**
 * Created by jake on 17. 4. 26.
 */
public class ShareStatResult {
    private String statName;

    private BigInteger statCount;

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
