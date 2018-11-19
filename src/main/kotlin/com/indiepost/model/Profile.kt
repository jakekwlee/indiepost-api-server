package com.indiepost.model

import com.indiepost.enums.Types
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "Profile")
data class Profile(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false)
        @Size(max = 50)
        var fullName: String = "No name",

        @NotNull
        @Column(nullable = false)
        @Size(max = 50)
        var displayName: String = "No name",

        @NotNull
        @Column(nullable = false, unique = true)
        @Size(max = 30)
        var slug: String,

        @Size(max = 50)
        var email: String = "email@example.com",

        var showEmail: Boolean = false,

        @Size(max = 50)
        var subEmail: String? = null,

        @Size(max = 100)
        var label: String = "Writer",

        var showLabel: Boolean = true,

        @Column(columnDefinition = "TEXT")
        var description: String? = null,

        var showDescription: Boolean = false,

        @Size(max = 20)
        var phone: String? = null,

        @URL
        @Size(max = 500)
        var picture: String? = null,

        var showPicture: Boolean = false,

        @NotNull
        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var profileType: Types.ProfileType = Types.ProfileType.Editor,

        @NotNull
        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var profileState: Types.ProfileState = Types.ProfileState.ACTIVE,

        @Column(columnDefinition = "TEXT")
        var etc: String? = null,

        @Column(nullable = false)
        var created: LocalDateTime? = null,

        @Column(nullable = false)
        var lastUpdated: LocalDateTime? = null
) {
    @OneToMany(
            mappedBy = "profile",
            cascade = [CascadeType.ALL],
            orphanRemoval = true)
    @OrderBy("id desc")
    var postProfile: MutableList<PostProfile> = ArrayList()
}
