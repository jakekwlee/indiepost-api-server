package com.indiepost.dto.post;

public class FullTextSearchQuery {

    private String text;

    private String status = "PUBLISH";

    private int page = 0;

    private int maxResults = 100;

    public FullTextSearchQuery() {
    }

    public FullTextSearchQuery(String text) {
        this.text = text;
    }

    public FullTextSearchQuery(String text, String status, int page, int maxResults) {
        this.text = text;
        this.status = status;
        this.page = page;
        this.maxResults = maxResults;
    }

    public FullTextSearchQuery(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
