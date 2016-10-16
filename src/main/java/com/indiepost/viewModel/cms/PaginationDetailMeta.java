package com.indiepost.viewModel.cms;

/**
 * Created by jake on 10/8/16.
 */
public class PaginationDetailMeta {

    private static final long serialVersionUID = 1L;

    private int pageAt = 0;

    private int postPerPage = 50;

    private int count;

    private boolean descending = true;

    public int getPageAt() {
        return pageAt;
    }

    public void setPageAt(int pageAt) {
        this.pageAt = pageAt;
    }

    public int getPostPerPage() {
        return postPerPage;
    }

    public void setPostPerPage(int postPerPage) {
        this.postPerPage = postPerPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }
}
