package com.indiepost.dto.post;

import java.util.ArrayList;
import java.util.List;

public class RelatedPostsMatchingResult {
    private String content;

    private List<Long> ids = new ArrayList<>();

    public RelatedPostsMatchingResult(String content) {
        this.content = content;
    }

    public RelatedPostsMatchingResult(String content, List<Long> ids) {

        this.content = content;
        this.ids = ids;
    }

    public RelatedPostsMatchingResult() {

    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
