package com.indiepost.dto;

import com.indiepost.enums.PostEnum;

import java.util.Date;

/**
 * Created by jake on 17. 1. 10.
 */
public class PostQuery {

    private Long authorId;

    private Long editorId;

    private String titleContains;

    private String contentContains;

    private String displayNameContains;

    private Long tagId;

    private String tagNameContains;

    private Long categoryId;

    private String categorySlug;

    private Date dateFrom;

    private Date dateTo;

    private int page = 0;

    private int maxResults = 30;

    private PostEnum.Status status = PostEnum.Status.PUBLISH;

    private PostEnum.Type type = PostEnum.Type.POST;

    private boolean featured = false;

    private boolean picked = false;

    private boolean splash = false;

    private int viewportWidth;

    private int viewportHeight;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public String getTitleContains() {
        return titleContains;
    }

    public void setTitleContains(String titleContains) {
        this.titleContains = titleContains;
    }

    public String getContentContains() {
        return contentContains;
    }

    public void setContentContains(String contentContains) {
        this.contentContains = contentContains;
    }

    public String getDisplayNameContains() {
        return displayNameContains;
    }

    public void setDisplayNameContains(String displayNameContains) {
        this.displayNameContains = displayNameContains;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagNameContains() {
        return tagNameContains;
    }

    public void setTagNameContains(String tagNameContains) {
        this.tagNameContains = tagNameContains;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public PostEnum.Status getStatus() {
        return status;
    }

    public void setStatus(PostEnum.Status status) {
        this.status = status;
    }

    public PostEnum.Type getType() {
        return type;
    }

    public void setType(PostEnum.Type type) {
        this.type = type;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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

    public int getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }
}
