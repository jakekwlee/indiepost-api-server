package com.indiepost.model;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private Long postId;

    private Long pageviews;

    private Long uniquePageviews;

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
}
