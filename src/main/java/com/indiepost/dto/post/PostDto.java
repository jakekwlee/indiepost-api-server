package com.indiepost.dto.post;

import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.TagDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 1. 22.
 */
public class PostDto extends PostSummaryDto {

    private String content;

    private List<TagDto> tags = new ArrayList<>();

    private List<ContributorDto> contributors = new ArrayList<>();

    public List<ContributorDto> getContributors() {
        return contributors;
    }

    public void setContributors(List<ContributorDto> contributors) {
        this.contributors = contributors;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

}
