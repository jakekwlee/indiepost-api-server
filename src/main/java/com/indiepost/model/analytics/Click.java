package com.indiepost.model.analytics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by jake on 8/7/17.
 */
@Entity
@Table(name = "Clicks", indexes = {
        @Index(columnList = "reqAt", name = "s_req_idx")
})
public class Click {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime reqAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "linkId")
    private Link link;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitorId")
    private Visitor visitor;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReqAt() {
        return reqAt;
    }

    public void setReqAt(LocalDateTime reqAt) {
        this.reqAt = reqAt;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
}
