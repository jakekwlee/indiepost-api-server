package com.indiepost.dto.stat;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 27.
 */
public class PeriodDto {

    private LocalDateTime since;
    private LocalDateTime until;

    public PeriodDto() {

    }

    public PeriodDto(LocalDateTime since, LocalDateTime until) {
        this.since = since;
        this.until = until;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }
}
