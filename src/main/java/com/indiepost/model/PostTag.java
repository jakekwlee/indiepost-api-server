package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 16. 12. 5.
 */
@Entity
@Table(name = "PostTag")
public class PostTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
