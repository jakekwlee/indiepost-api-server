package com.indiepost.dto;

import java.util.List;

public class Highlight {

    private String title;

    private String excerpt;

    private List<String> contributors;

    private List<String> tags;

    private String bylineName;

    public Highlight() {
    }

    public Highlight(String title) {

        this.title = title;
    }

    public Highlight(String title, String excerpt) {

        this.title = title;
        this.excerpt = excerpt;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getBylineName() {
        return bylineName;
    }

    public void setBylineName(String bylineName) {
        this.bylineName = bylineName;
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
