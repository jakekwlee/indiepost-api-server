package com.indiepost.repository

import com.indiepost.model.analytics.Pageview

/**
 * Created by jake on 17. 4. 17.
 */
interface PageviewRepository {
    fun save(pageview: Pageview)
}
