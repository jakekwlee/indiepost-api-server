package com.indiepost.dto;

public class DeleteResponse {

    private Long deletedId;

    public DeleteResponse() {

    }

    public DeleteResponse(Long deletedId) {
        this.deletedId = deletedId;
    }

    public Long getDeletedId() {
        return deletedId;
    }

    public void setDeletedId(Long deletedId) {
        this.deletedId = deletedId;
    }
}
