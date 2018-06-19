package com.indiepost.dto;

public class UserReadDto {
    private Long userId;

    private Long postId;

    public UserReadDto(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public UserReadDto() {

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
}
