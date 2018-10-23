package com.indiepost.model

import com.indiepost.enums.Types
import javax.persistence.*

/**
 * Created by jake on 7/27/16.
 */
@Entity
@Table(name = "Roles")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Enumerated(EnumType.STRING)
        var roleType: Types.UserRole? = null,

        var level: Int = 1
) {
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    var users: MutableList<User>? = null
}
