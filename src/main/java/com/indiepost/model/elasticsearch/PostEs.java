package com.indiepost.model.elasticsearch;

import io.searchbox.annotations.JestId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostEs implements Serializable {

    private static final long serialVersionUID = 6587432935142567307L;

    @JestId
    private Long id;

    private String title;

    private String excerpt;

    private String content;

    private String bylineName;

    private List<String> contributors = new ArrayList<>();

    private List<String> tags = new ArrayList<>();

    private String status;

    public PostEs(Long id, String title, String excerpt) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
    }

    public PostEs() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBylineName() {
        return bylineName;
    }

    public void setBylineName(String bylineName) {
        this.bylineName = bylineName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public void addContributor(String contributor) {
        contributors.add(contributor);
    }

    public void removeContributor(String contributor) {
        contributors.remove(contributor);
    }
}
