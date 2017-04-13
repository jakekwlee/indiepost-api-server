package com.indiepost.model;

import com.indiepost.enums.Types;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jake on 17. 4. 13.
 */
//@Entity
//@Table(name = "Pageviews")
public class Pageview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    private String path;

    @NotNull
    @Size(max = 10)
    private Types.Pageview type;

    @Size(max = 500)
    private String referrer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sessionId", updatable = false, insertable = false, nullable = false)
    private Visitor visitor;

    @ManyToOne(optional = false)
    private Long sessionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "postId", updatable = false, insertable = false, nullable = false)
    private Post post;

    @ManyToOne(optional = false)
    private Long postId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Types.Pageview getType() {
        return type;
    }

    public void setType(Types.Pageview type) {
        this.type = type;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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
