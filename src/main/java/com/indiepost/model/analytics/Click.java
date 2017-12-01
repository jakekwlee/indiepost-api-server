package com.indiepost.model.analytics;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by jake on 8/7/17.
 */
@Entity
@DiscriminatorValue("Click")
public class Click extends Stat implements Serializable {

    private static final long serialVersionUID = -20963478153112462L;

    @ManyToOne
    @JoinColumn(name = "linkId")
    private Link link;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
