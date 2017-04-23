package com.indiepost.model;

import com.indiepost.enums.Types.ActionType;
import com.indiepost.enums.Types.StatType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jake on 17. 4. 13.
 */
@Entity
@Table(name = "Stats")
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    private String path;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatType type;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    @Size(max = 500)
    private String referrer;

    @Size(max = 30)
    private String label;

    private Integer value;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitorId", updatable = false, insertable = false, nullable = false)
    private Visitor visitor;

    @NotNull
    @Column(name = "visitorId", nullable = false)
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

    public StatType getType() {
        return type;
    }

    public void setType(StatType type) {
        this.type = type;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
}
