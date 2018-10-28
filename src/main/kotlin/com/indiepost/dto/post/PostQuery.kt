package com.indiepost.dto.post

import com.indiepost.enums.Types.PostStatus
import com.indiepost.utils.DateUtil.instantToLocalDateTime
import java.time.Instant
import java.time.LocalDateTime

/**
 * Created by jake on 17. 1. 10.
 */
class PostQuery private constructor() {

    var category: String? = null
        private set

    var displayName: String? = null
        private set

    var text: String? = null
        private set

    var status: PostStatus? = null
        private set

    var featured: Boolean? = null
        private set

    var picked: Boolean? = null
        private set

    var splash: Boolean? = null
        private set

    var modifiedAfter: LocalDateTime? = null
        private set

    var modifiedBefore: LocalDateTime? = null
        private set

    var publishedAfter: LocalDateTime? = null
        private set

    var publishedBefore: LocalDateTime? = null
        private set

    class Builder {
        private var status: PostStatus? = null

        private var category: String? = null

        private var displayName: String? = null

        private var text: String? = null

        private var featured: Boolean? = null

        private var picked: Boolean? = null

        private var splash: Boolean? = null

        private var modifiedAfter: Instant? = null

        private var modifiedBefore: Instant? = null

        private var publishedAfter: Instant? = null

        private var publishedBefore: Instant? = null

        constructor(status: PostStatus) {
            this.status = status
        }

        constructor(status: String) {
            this.status = PostStatus.valueOf(status)
        }

        fun category(category: String): Builder {
            this.category = category
            return this
        }

        fun displayName(displayName: String): Builder {
            this.displayName = displayName
            return this
        }

        fun searchText(text: String): Builder {
            this.text = text
            return this
        }

        fun featured(isFeatured: Boolean): Builder {
            this.featured = isFeatured
            return this
        }

        fun picked(isPicked: Boolean): Builder {
            this.picked = isPicked
            return this
        }

        fun splash(isSplash: Boolean): Builder {
            this.splash = isSplash
            return this
        }

        fun publishedAfter(publishedAfter: Instant): Builder {
            this.publishedAfter = publishedAfter
            return this
        }

        fun publishedBefore(publishedBefore: Instant): Builder {
            this.publishedBefore = publishedBefore
            return this
        }

        fun modifiedAfter(modifiedAfter: Instant): Builder {
            this.modifiedAfter = modifiedAfter
            return this
        }

        fun modifiedBefore(modifiedBefore: Instant): Builder {
            this.modifiedBefore = modifiedBefore
            return this
        }

        fun build(): PostQuery {
            val query = PostQuery()

            // Required
            query.status = status

            if (category != null) {
                query.category = category
            }
            if (displayName != null) {
                query.displayName = displayName
            }
            if (text != null) {
                query.text = text
            }
            if (featured != null) {
                query.featured = featured
            }
            if (picked != null) {
                query.picked = picked
            }
            if (splash != null) {
                query.splash = splash
            }
            publishedAfter?.let {
                query.publishedAfter = instantToLocalDateTime(it)
            }
            publishedBefore?.let {
                query.publishedBefore = instantToLocalDateTime(it)
            }
            modifiedBefore?.let {
                query.modifiedBefore = instantToLocalDateTime(it)
            }
            modifiedAfter?.let {
                query.modifiedAfter = instantToLocalDateTime(it)
            }
            return query
        }
    }
}
