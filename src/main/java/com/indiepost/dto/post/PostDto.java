package com.indiepost.dto.post;

import com.indiepost.dto.ContributorDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class PostDto extends PostSummaryDto implements Serializable {

    private String content;

    private List<String> tags = new ArrayList<>();

    private List<ContributorDto> contributors = new ArrayList<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ContributorDto> getContributors() {
        return contributors;
    }

    public void setContributors(List<ContributorDto> contributors) {
        this.contributors = contributors;
    }
}
