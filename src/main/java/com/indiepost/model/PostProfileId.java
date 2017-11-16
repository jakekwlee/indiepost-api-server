package com.indiepost.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostProfileId implements Serializable {
    @Column
    private Long postId;

    @Column
    private Long profileId;

    private PostProfileId() {
    }

    public PostProfileId(Long postId, Long profileId) {
        this.postId = postId;
        this.profileId = profileId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, profileId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PostProfileId that = (PostProfileId) obj;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(profileId, that.profileId);
    }
}
