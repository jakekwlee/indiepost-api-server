package com.indiepost.controller.admin

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/cache")
class AdminCacheController {

    @GetMapping("/home")
    @CacheEvict(cacheNames = ["home::rendered"], key = "0")
    fun clearHomepageCache() {
        logger.info("Clear cache: home::rendered::0")
    }


    @GetMapping("/page/{slug}")
    @CacheEvict(cacheNames = ["static-page::rendered"], key = "#slug")
    fun clearStaticPageCache(@PathVariable slug: String) {
        logger.info("Clear cache: static-page::rendered::$slug")
    }

    @GetMapping("/post/{id}")
    @CacheEvict(cacheNames = ["post::rendered"], key = "#id")
    fun clearPostCache(@PathVariable id: Long) {
        logger.info("Clear cache: post::rendered::$id")
    }

    @GetMapping("/latest")
    @CacheEvict(cacheNames = ["latest::rendered"], allEntries = true)
    fun clearLatestCache() {
        logger.info("Clear cache: latest::rendered::all")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AdminCacheController::class.java)
    }
}
