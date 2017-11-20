package com.indiepost.dto;

import java.io.Serializable;

public class ImageSetDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String original;

    private String large;

    private String optimized;

    private String small;

    private String thumbnail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getOptimized() {
        return optimized;
    }

    public void setOptimized(String optimized) {
        this.optimized = optimized;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
