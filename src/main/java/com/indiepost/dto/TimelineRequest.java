package com.indiepost.dto;

public class TimelineRequest {
    private long timepoint;

    private boolean after;

    private int size;

    public TimelineRequest() {
    }

    public long getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(long timepoint) {
        this.timepoint = timepoint;
    }

    public boolean isAfter() {
        return after;
    }

    public void setAfter(boolean after) {
        this.after = after;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
