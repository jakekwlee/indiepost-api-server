package com.indiepost.viewModel.cms;

/**
 * Created by jake on 10/8/16.
 */
public class PaginationDetailMeta {

    private static final long serialVersionUID = 1L;

    private Integer pageAt = 0;

    private Integer postPerPage = 50;

    private Long count;

    private boolean descending = true;

    public Integer getPageAt() {
        return pageAt;
    }

    public void setPageAt(Integer pageAt) {
        this.pageAt = pageAt;
    }

    public Integer getPostPerPage() {
        return postPerPage;
    }

    public void setPostPerPage(Integer postPerPage) {
        this.postPerPage = postPerPage;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }
}
