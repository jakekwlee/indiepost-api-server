package com.indiepost.dto.admin;

/**
 * Created by jake on 16. 12. 1.
 */
public class AdminAutosaveResponseDto {

    private Long id;

    private Long originalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }
}
