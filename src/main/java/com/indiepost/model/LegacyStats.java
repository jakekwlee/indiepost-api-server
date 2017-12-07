package com.indiepost.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LegacyStats")
public class LegacyStats implements Serializable {

    private static final long serialVersionUID = 6794349083351279587L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "postId")
    private Post post;

    @Column(nullable = false)
    private Long uniquePageviews;

    @Column(nullable = false)
    private Long pageviews;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getUniquePageviews() {
        return uniquePageviews;
    }

    public void setUniquePageviews(Long uniquePageviews) {
        this.uniquePageviews = uniquePageviews;
    }

    public Long getPageviews() {
        return pageviews;
    }

    public void setPageviews(Long pageviews) {
        this.pageviews = pageviews;
    }
}
