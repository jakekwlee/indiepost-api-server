package com.indiepost.dto;

import com.indiepost.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class PostDto extends PostSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    private List<Tag> tags = new ArrayList<>();

    private List<Long> relatedPostIds = new ArrayList<>();

    public List<Long> getRelatedPostIds() {
        return relatedPostIds;
    }

    public void setRelatedPostIds(List<Long> relatedPostIds) {
        this.relatedPostIds = relatedPostIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
