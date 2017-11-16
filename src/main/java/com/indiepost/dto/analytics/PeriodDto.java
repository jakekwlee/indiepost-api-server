package com.indiepost.dto.analytics;

import com.indiepost.enums.Types;

import java.time.LocalDate;

/**
 * Created by jake on 17. 4. 27.
 */
public class PeriodDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private Types.TimeDomainDuration duration;

    public PeriodDto() {

    }

    public PeriodDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = Types.TimeDomainDuration.DAILY;
    }

    public PeriodDto(LocalDate startDate, LocalDate endDate, Types.TimeDomainDuration duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;

    }

    public Types.TimeDomainDuration getDuration() {
        return duration;
    }

    public void setDuration(Types.TimeDomainDuration duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
