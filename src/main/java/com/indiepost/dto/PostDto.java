package com.indiepost.dto;

import com.indiepost.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class PostDto extends PostSummaryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    private List<Tag> tags = new ArrayList<>();

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
