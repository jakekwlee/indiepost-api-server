package com.indiepost.repository

import com.indiepost.model.analytics.Action

/**
 * Created by jake on 8/9/17.
 */
interface ActionRepository {
    fun save(action: Action)
}
