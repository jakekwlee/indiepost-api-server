package com.indiepost.dto.analytics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by jake on 8/10/17.
 */
public class CampaignDto {

    private Long id;

    @NotNull
    @Min(2)
    @Max(30)
    private String clientName;

    @NotNull
    @Min(2)
    @Max(30)
    private String name;

    @NotNull
    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime startAt;

    @NotNull
    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime endAt;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    private LocalDateTime createAt;

    @NotNull
    private Long goal = 0L;

    private Long validClicks = 0L;

    private Long allClicks = 0L;

    public Long getAllClicks() {
        return allClicks;
    }

    public void setAllClicks(Long allClicks) {
        this.allClicks = allClicks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getGoal() {
        return goal;
    }

    public void setGoal(Long goal) {
        this.goal = goal;
    }

    public Long getValidClicks() {
        return validClicks;
    }

    public void setValidClicks(Long validClicks) {
        this.validClicks = validClicks;
    }

}
