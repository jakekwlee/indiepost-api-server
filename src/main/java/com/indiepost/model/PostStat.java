package com.indiepost.model;

import javax.persistence.*;

/**
 * Created by jake on 10/23/17.
 */
@Entity
@Table(name = "PostStats")
public class PostStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long pageview;

    @Column(nullable = false)
    private Long uniquePageview;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
