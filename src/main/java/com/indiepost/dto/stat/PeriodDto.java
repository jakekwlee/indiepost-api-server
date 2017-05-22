package com.indiepost.dto.stat;

import java.time.ZonedDateTime;

/**
 * Created by jake on 17. 4. 27.
 */
public class PeriodDto {

    private ZonedDateTime since;

    private ZonedDateTime until;

    public PeriodDto() {

    }

    public PeriodDto(ZonedDateTime since, ZonedDateTime until) {
        this.since = since;
        this.until = until;
    }

    public ZonedDateTime getSince() {
        return since;
    }

    public void setSince(ZonedDateTime since) {
        this.since = since;
    }

    public ZonedDateTime getUntil() {
        return until;
    }

    public void setUntil(ZonedDateTime until) {
        this.until = until;
    }
}
