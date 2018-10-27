package com.indiepost.service

import com.indiepost.dto.InitialData

/**
 * Created by jake on 17. 1. 22.
 */
interface InitialDataService {
    fun getInitialData(withLatestPosts: Boolean): InitialData
}
