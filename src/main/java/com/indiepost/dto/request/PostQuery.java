package com.indiepost.dto.request;

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

    private String categoryId;

    private String categorySlug;

    private PostEnum.Status status;

    private PostEnum.Type type;

    private Date dateFrom;

    private Date dateTo;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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
}
