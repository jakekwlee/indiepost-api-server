package com.indiepost.dto;

import java.util.List;

/**
 * Created by jake on 17. 1. 24.
 */
public class RelatedPostsRequestDto {

    private List<Long> postIds;


    public List<Long> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<Long> postIds) {
        this.postIds = postIds;
    }
}
