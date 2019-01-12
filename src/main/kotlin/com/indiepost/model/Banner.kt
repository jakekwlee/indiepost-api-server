package com.indiepost.model

import com.indiepost.enums.Types.BannerTarget
import com.indiepost.enums.Types.BannerType
import com.indiepost.model.analytics.Link

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "Banners")
data class Banner(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Size(max = 30)
        var title: String? = null,

        @Size(max = 30)
        var subtitle: String? = null,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var bannerType: BannerType? = null,

        @Column(nullable = false)
        @Size(max = 12)
        var bgColor: String? = "#ccc",

        @Size(max = 500)
        var imageUrl: String? = null,

        @Column(nullable = false)
        var isCover: Boolean = false,

        var internalUrl: String? = null,

        @Column(nullable = false)
        var priority: Int = 0,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var target: BannerTarget = BannerTarget.All,

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "linkId")
        var link: Link? = null,

        @Column(name = "linkId", insertable = false, updatable = false)
        var linkId: Long? = null
)


