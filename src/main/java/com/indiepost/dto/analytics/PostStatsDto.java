package com.indiepost.dto.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 10/29/17.
 */
public class PostStatsDto {

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime lastUpdated;

    private List<PostStatDto> statData;

    public PostStatsDto() {
    }

    public PostStatsDto(List<PostStatDto> statData) {
        this.statData = statData;
    }

    public PostStatsDto(LocalDateTime lastUpdated, List<PostStatDto> statData) {
        this.lastUpdated = lastUpdated;
        this.statData = statData;
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
}