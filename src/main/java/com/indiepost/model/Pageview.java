package com.indiepost.model;

import com.indiepost.enums.Types;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jake on 17. 4. 13.
 */
@Entity
@Table(name = "Pageviews")
public class Pageview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    private String path;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Types.ContentType contentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Types.RequestType requestType = Types.RequestType.WEBPAGE;

    @Size(max = 500)
    private String referrer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitorId", updatable = false, insertable = false, nullable = false)
    private Visitor visitor;

    @Column(name = "visitorId")
    private Long visitorId;

    @ManyToOne
    @JoinColumn(name = "postId", updatable = false, insertable = false)
    private Post post;

    @Column(name = "postId")
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

    public Types.ContentType getContentType() {
        return contentType;
    }

    public void setContentType(Types.ContentType contentType) {
        this.contentType = contentType;
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

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
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

    public Types.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(Types.RequestType requestType) {
        this.requestType = requestType;
    }
}
