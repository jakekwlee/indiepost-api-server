package com.indiepost.dto.stat;

/**
 * Created by jake on 17. 5. 10.
 */
public class PostStat {
    private Long id;

    private String title;

    private String category;

    private String author;

    private Long pageview;

    private Long uniquePageview;

    public PostStat() {
    }

    public PostStat(Long id, String title, String category, String author, Long pageview, Long uniquePageview) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.author = author;
        this.pageview = pageview;
        this.uniquePageview = uniquePageview;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getPageview() {
        return pageview;
    }

    public void setPageview(Long pageview) {
        this.pageview = pageview;
    }

    public Long getUniquePageview() {
        return uniquePageview;
    }

    public void setUniquePageview(Long uniquePageview) {
        this.uniquePageview = uniquePageview;
    }
}
