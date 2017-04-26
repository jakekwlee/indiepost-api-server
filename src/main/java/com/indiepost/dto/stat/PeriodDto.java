package com.indiepost.dto.stat;

import java.util.Date;

/**
 * Created by jake on 17. 4. 27.
 */
public class PeriodDto {
    private Date since;

    private Date until;

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }
}
