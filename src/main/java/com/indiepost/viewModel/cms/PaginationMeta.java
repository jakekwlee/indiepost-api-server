package com.indiepost.viewModel.cms;

/**
 * Created by jake on 10/8/16.
 */
public class PaginationMeta {

    private static final long serialVersionUID = 1L;

    private PaginationDetailMeta published;

    private PaginationDetailMeta booked;

    private PaginationDetailMeta queued;

    private PaginationDetailMeta draft;

    private PaginationDetailMeta deleted;

    public PaginationDetailMeta getPublished() {
        return published;
    }

    public void setPublished(PaginationDetailMeta published) {
        this.published = published;
    }

    public PaginationDetailMeta getQueued() {
        return queued;
    }

    public PaginationDetailMeta getBooked() {
        return booked;
    }

    public void setBooked(PaginationDetailMeta booked) {
        this.booked = booked;
    }

    public void setQueued(PaginationDetailMeta queued) {
        this.queued = queued;
    }

    public PaginationDetailMeta getDraft() {
        return draft;
    }

    public void setDraft(PaginationDetailMeta draft) {
        this.draft = draft;
    }

    public PaginationDetailMeta getDeleted() {
        return deleted;
    }

    public void setDeleted(PaginationDetailMeta deleted) {
        this.deleted = deleted;
    }
}
