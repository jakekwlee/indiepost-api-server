package com.indiepost.model.analytics;

import javax.persistence.*;

/**
 * Created by jake on 8/7/17.
 */
@Entity
@DiscriminatorValue("Click")
public class Click extends Stat {

    @ManyToOne
    @JoinColumn(name = "linkId")
    private Link link;

    @Column(updatable = false, insertable = false)
    private Long linkId;

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
