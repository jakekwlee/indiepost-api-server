package com.indiepost.dto;

public class DeletedResponse {
    private Long id;

    public DeletedResponse() {
    }

    public DeletedResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
