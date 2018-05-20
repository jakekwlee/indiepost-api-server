package com.indiepost.dto.post;

import com.indiepost.enums.Types;

import java.util.List;

public class BulkStatusUpdateDto {
    private List<Long> ids;

    private String status = Types.PostStatus.DRAFT.toString();

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
