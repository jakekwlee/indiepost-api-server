package com.indiepost.dto;

public class Highlight {

    private String title;

    private String excerpt;

    public Highlight() {
    }

    public Highlight(String title) {

        this.title = title;
    }

    public Highlight(String title, String excerpt) {

        this.title = title;
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
}
