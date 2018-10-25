package com.indiepost.repository

import com.indiepost.model.Setting

/**
 * Created by jake on 8/31/17.
 */
interface SettingRepository {

    fun get(): Setting

    fun update(setting: Setting)
}
