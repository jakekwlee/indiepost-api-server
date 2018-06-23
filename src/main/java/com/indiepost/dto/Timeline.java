package com.indiepost.dto;

import java.time.Instant;
import java.util.List;

public class Timeline<T> {
    private final List<T> content;

    private final int numberOfElements;

    private final int totalElements;

    private final boolean last;

    private final int size;

    private final boolean after;

    private final Instant timepoint;

    public Timeline(List<T> content, TimelineRequest request, int total) {
        this.content = content;
        this.totalElements = total;
        this.numberOfElements = content.size();
        this.last = total == content.size();
        this.size = request.getSize();
        this.after = request.isAfter();
        this.timepoint = request.getTimepoint();
    }

    public List<T> getContent() {
        return content;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public int getSize() {
        return size;
    }

    public boolean isAfter() {
        return after;
    }

    public Instant getTimepoint() {
        return timepoint;
    }
}
