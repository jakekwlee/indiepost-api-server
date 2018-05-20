package com.indiepost.dto;

public class CreateResponse {
    private Long id;

    private Long originalId;

    public CreateResponse(Long id, Long originalId) {
        this.id = id;
        this.originalId = originalId;
    }

    public CreateResponse(Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
