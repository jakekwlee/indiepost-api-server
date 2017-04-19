package com.indiepost.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by jake on 17. 4. 19.
 */
public class PageviewDto {

    @NotNull
    private String path;

    @NotNull
    private String contentType;

    @NotNull
    private String client;

    private String referrer;

    private Long userId;

    private Long postId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
