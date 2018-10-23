package com.indiepost.model.analytics

import javax.persistence.*

/**
 * Created by jake on 8/7/17.
 */
@Entity
@DiscriminatorValue("Click")
class Click : Stat() {
    @ManyToOne
    @JoinColumn(name = "linkId")
    var link: Link? = null

    @Column(updatable = false, insertable = false)
    var linkId: Long? = null
}
