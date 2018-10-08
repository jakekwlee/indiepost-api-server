package com.indiepost.model.analytics

import com.indiepost.enums.Types.LinkType
import com.indiepost.model.Banner
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by jake on 8/6/17.
 */
@Entity
@Table(name = "Links")
data class Link(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false)
        var name: String? = null,

        @Column(nullable = false, unique = true)
        var uid: String? = null,

        @NotNull
        @Column(nullable = false)
        var url: String? = null,

        @NotNull
        @Column(nullable = false)
        var createdAt: LocalDateTime? = null,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "link")
        @LazyCollection(LazyCollectionOption.EXTRA)
        var clicks: MutableList<Click>? = null,

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "campaignId")
        var campaign: Campaign? = null,

        @Column(name = "campaignId", nullable = false, updatable = false, insertable = false)
        var campaignId: Long? = null,

        @Enumerated(EnumType.STRING)
        var linkType: LinkType = LinkType.Standard,

        @OneToOne(mappedBy = "link", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
        var banner: Banner? = null

) : Serializable {
    companion object {
        private const val serialVersionUID = 6556838761006808928L
    }
}
