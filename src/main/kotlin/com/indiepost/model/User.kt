package com.indiepost.model

import com.indiepost.enums.Types.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false, unique = true)
        @Size(min = 3, max = 200)
        var username: String? = null,

        @Size(max = 500)
        var profile: String? = null,

        @Size(max = 500)
        var picture: String? = null,

        @Column(unique = true)
        @Size(min = 7, max = 50)
        var email: String? = null,

        @Column(nullable = false)
        @Size(min = 2, max = 30)
        var displayName: String? = null,

        @Column(nullable = false)
        var joinedAt: LocalDateTime? = null,

        @Column(nullable = false)
        var updatedAt: LocalDateTime? = null,

        @Column(nullable = false)
        var lastLogin: LocalDateTime? = null,

        @Pattern(regexp = "^01[\\d]{8,9}")
        @Size(min = 7, max = 15)
        var phone: String? = null,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var state: UserState = UserState.ACTIVATED,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var gender: UserGender? = null,


        @Enumerated(EnumType.STRING)
        var roleType: UserRole? = null
) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    var postReadings: MutableList<PostReading>? = null

    @Column(nullable = false)
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
            name = "Users_Roles",
            joinColumns = [JoinColumn(name = "userId")],
            inverseJoinColumns = [JoinColumn(name = "roleId")]
    )
    var roles: MutableList<Role> = ArrayList()

    val highestRole: UserRole
        get() {
            var userLevel = 1
            for (role in roles) {
                if (role.level > userLevel) {
                    userLevel = role.level
                }
            }
            when (userLevel) {
                9 -> return UserRole.Administrator
                7 -> return UserRole.EditorInChief
                5 -> return UserRole.Editor
                3 -> return UserRole.Author
                1 -> return UserRole.User
                else -> return UserRole.User
            }
        }
}
