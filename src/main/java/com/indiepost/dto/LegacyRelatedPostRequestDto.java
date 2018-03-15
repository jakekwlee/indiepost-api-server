package com.indiepost.dto;

import java.util.List;

public class LegacyRelatedPostRequestDto {

    private List<Long> postIds;

    private boolean isMobile;

    private boolean isLegacy;

    public List<Long> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<Long> postIds) {
        this.postIds = postIds;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isLegacy() {
        return isLegacy;
    }

    public void setLegacy(boolean legacy) {
        isLegacy = legacy;
    }
}
