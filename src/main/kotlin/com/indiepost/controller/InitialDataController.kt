package com.indiepost.controller

import com.indiepost.dto.InitialData
import com.indiepost.service.InitialDataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.inject.Inject

/**
 * Created by jake on 17. 1. 22.
 */
@RestController
@RequestMapping("/init")
class InitialDataController @Inject
constructor(private val initialDataService: InitialDataService) {

    @GetMapping
    fun getInitialData(): InitialData = initialDataService.getInitialData(withLatestPosts = true)
}
