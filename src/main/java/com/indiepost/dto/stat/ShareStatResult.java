package com.indiepost.dto.stat;

import java.math.BigInteger;

/**
 * Created by jake on 17. 4. 26.
 */
public class ShareStatResult {
    private String name;

    private BigInteger statCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getStatCount() {
        return statCount;
    }

    public void setStatCount(BigInteger statCount) {
        this.statCount = statCount;
    }
}
