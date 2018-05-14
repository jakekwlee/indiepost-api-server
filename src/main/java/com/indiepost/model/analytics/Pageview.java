package com.indiepost.model.analytics;

import com.indiepost.model.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jake on 8/9/17.
 */
@Entity
@DiscriminatorValue("Pageview")
public class Pageview extends Stat {

    @NotNull
    @Column(nullable = false, columnDefinition = "bit(1) default b'0'")
    private Boolean isLandingPage = false;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Column(name = "postId", updatable = false, insertable = false)
    private Long postId;

    public Boolean getLandingPage() {
        return isLandingPage;
    }

    public void setLandingPage(Boolean landingPage) {
        isLandingPage = landingPage;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
