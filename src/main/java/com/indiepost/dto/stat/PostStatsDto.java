package com.indiepost.dto.stat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 10/29/17.
 */
public class PostStatsDto {

    private LocalDateTime lastUpdated;

    private List<PostStatDto> statData;

    private PeriodDto period;

    public PostStatsDto() {
    }

    public PostStatsDto(List<PostStatDto> statData) {
        this.statData = statData;
    }

    public PostStatsDto(LocalDateTime lastUpdated, List<PostStatDto> statData) {
        this.lastUpdated = lastUpdated;
        this.statData = statData;
    }

    public PostStatsDto(LocalDateTime lastUpdated, List<PostStatDto> statData, PeriodDto period) {
        this.lastUpdated = lastUpdated;
        this.statData = statData;
        this.period = period;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<PostStatDto> getStatData() {
        return statData;
    }

    public void setStatData(List<PostStatDto> statData) {
        this.statData = statData;
    }

    public PeriodDto getPeriod() {
        return period;
    }

    public void setPeriod(PeriodDto period) {
        this.period = period;
    }
}
