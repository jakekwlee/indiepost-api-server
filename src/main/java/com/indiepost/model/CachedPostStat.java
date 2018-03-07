package com.indiepost.model;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jake on 10/29/17.
 */
@Entity
@Table(name = "CachedPostStats")
public class CachedPostStat implements Serializable {

    private static final long serialVersionUID = -8011193195679356884L;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId")
    private Post post;

    private Long pageviews;

    private Long uniquePageviews;

    private Long legacyPageviews;

    private Long legacyUniquePageviews;

    public Long getLegacyPageviews() {
        return legacyPageviews;
    }

    public void setLegacyPageviews(Long legacyPageviews) {
        this.legacyPageviews = legacyPageviews;
    }

    public Long getLegacyUniquePageviews() {
        return legacyUniquePageviews;
    }

    public void setLegacyUniquePageviews(Long legacyUniquePageviews) {
        this.legacyUniquePageviews = legacyUniquePageviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPageviews() {
        return pageviews;
    }

    public void setPageviews(Long pageviews) {
        this.pageviews = pageviews;
    }

    public Long getUniquePageviews() {
        return uniquePageviews;
    }

    public void setUniquePageviews(Long uniquePageviews) {
        this.uniquePageviews = uniquePageviews;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}