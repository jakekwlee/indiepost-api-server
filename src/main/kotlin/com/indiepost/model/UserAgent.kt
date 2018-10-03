package com.indiepost.model

import com.sangupta.murmur.Murmur2

import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "UserAgents")
data class UserAgent(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Size(max = 2048)
        @Column(nullable = false, unique = true)
        var uaString: String? = null
) {
    @Column(nullable = false, unique = true)
    var uaHash: Long? = null
        private set

    fun setUaHash(uaString: String) {
        this.uaHash = Murmur2.hash64(uaString.toByteArray(), uaString.length, 1110L)
    }
}
