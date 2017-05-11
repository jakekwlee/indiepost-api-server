package com.indiepost.dto.stat;

import java.math.BigInteger;

/**
 * Created by jake on 17. 5. 10.
 */
public class PostStat {
    private BigInteger id;

    private String title;

    private String category;

    private String author;

    private BigInteger pageview;

    private BigInteger uniquePageview;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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

    public BigInteger getPageview() {
        return pageview;
    }

    public void setPageview(BigInteger pageview) {
        this.pageview = pageview;
    }

    public BigInteger getUniquePageview() {
        return uniquePageview;
    }

    public void setUniquePageview(BigInteger uniquePageview) {
        this.uniquePageview = uniquePageview;
    }
}
