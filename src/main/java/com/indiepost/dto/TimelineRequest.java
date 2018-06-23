package com.indiepost.dto;

import java.time.Instant;

public class TimelineRequest {
    private Instant timepoint;

    private boolean after;

    private int size;

    public TimelineRequest(Instant timepoint, boolean after, int size) {
        this.timepoint = timepoint;
        this.after = after;
        this.size = size;
    }

    public Instant getTimepoint() {
        return timepoint;
    }

    public boolean isAfter() {
        return after;
    }

    public int getSize() {
        return size;
    }
}
