package com.indiepost.dto.stat;

import java.time.LocalDate;

/**
 * Created by jake on 17. 4. 27.
 */
public class PeriodDto {

    private LocalDate startDate;

    private LocalDate endDate;

    public PeriodDto() {

    }

    public PeriodDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
