package com.indiepost.dto.post;

import com.indiepost.model.Contributor;
import com.indiepost.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class PostDto extends PostSummaryDto {

    private String content;

    private List<Tag> tags = new ArrayList<>();

    private List<Contributor> contributors = new ArrayList<>();

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

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }
}
