package com.indiepost.model

import com.indiepost.enums.Types.ContributorDisplayType
import com.indiepost.enums.Types.ContributorType
import org.hibernate.validator.constraints.URL
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Contributors")
data class Contributor(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @OneToMany(mappedBy = "contributor", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true)
        @OrderBy("id desc")
        var postContributors: MutableList<PostContributor> = ArrayList(),

        @NotNull
        @Column(nullable = false)
        @Size(max = 30)
        var fullName: String? = null,

        @Size(max = 50)
        var email: String = "email@example.com",

        @Size(max = 50)
        var subEmail: String? = null,

        @Size(max = 100)
        var title: String? = null,

        @Column(columnDefinition = "TEXT")
        var description: String? = null,

        @URL
        @Size(max = 500)
        var url: String? = null,

        var phone: String? = null,

        @URL
        @Size(max = 500)
        var picture: String? = null,

        @NotNull
        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var displayType: ContributorDisplayType? = null,

        @NotNull
        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var contributorType: ContributorType? = null,

        var etc: String? = null,

        var isTitleVisible: Boolean = false,

        var isEmailVisible: Boolean = false,

        var isDescriptionVisible: Boolean = false,

        var isUrlVisible: Boolean = false,

        var isPictureVisible: Boolean = false,

        var isPhoneVisible: Boolean = false,

        @Column(nullable = false)
        var created: LocalDateTime? = null,

        @Column(nullable = false)
        var lastUpdated: LocalDateTime? = null
) : Serializable {
    companion object {
        private val serialVersionUID = 7763721241430441486L
    }
}
