package com.indiepost.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostContributorId implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "contributor_id")
    private Long contributorId;

    private PostContributorId() {
    }

    public PostContributorId(Long postId, Long contributorId) {
        this.postId = postId;
        this.contributorId = contributorId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getContributorId() {
        return contributorId;
    }

    public void setContributorId(Long contributorId) {
        this.contributorId = contributorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, contributorId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostContributorId that = (PostContributorId) obj;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(contributorId, that.contributorId);
    }
}
