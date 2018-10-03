package com.indiepost.model.analytics

import java.io.Serializable
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by jake on 8/9/17.
 */
@Entity
@DiscriminatorValue("Action")
data class Action(
        @NotNull
        @Size(max = 50)
        var actionType: String? = null,

        @Size(max = 30)
        var label: String? = null,

        var value: Int? = null

) : Stat(), Serializable {
    companion object {
        private const val serialVersionUID = -527267775197998523L
    }
}
