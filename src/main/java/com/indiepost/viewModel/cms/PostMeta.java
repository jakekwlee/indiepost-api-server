package com.indiepost.viewModel.cms;

import java.util.Date;

/**
 * Created by jake on 10/8/16.
 */
public class PostMeta {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String status;

    private String authorName;

    private CategoryMeta category;

    private UserMeta author;

    private Date publishedAt;

    private Date createdAt;

    private int likedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CategoryMeta getCategory() {
        return category;
    }

    public void setCategory(CategoryMeta category) {
        this.category = category;
    }

    public UserMeta getAuthor() {
        return author;
    }

    public void setAuthor(UserMeta author) {
        this.author = author;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
