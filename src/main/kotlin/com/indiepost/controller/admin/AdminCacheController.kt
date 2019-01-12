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

    @GetMapping("/post/{id}")
    @CacheEvict(cacheNames = ["post::rendered"], key = "#id")
    fun clearPostCache(@PathVariable id: Long) {
        logger.info("Clear cache: post::rendered::$id")
    }

    @GetMapping("/page/{slug}")
    @CacheEvict(cacheNames = ["static-page::rendered"], key = "#slug")
    fun clearStaticPageCache(@PathVariable slug: String) {
        logger.info("Clear cache: static-page::rendered::$slug")
    }

    @GetMapping("/tag/{slug}")
    @CacheEvict(cacheNames = ["tag::rendered"], key = "#slug")
    fun clearCategoryCache(@PathVariable slug: String) {
        logger.info("Clear cache: tag::rendered::$slug")
    }

    @GetMapping("/home")
    @CacheEvict(cacheNames = ["home::rendered"], key = "0")
    fun clearHomepageCache() {
        logger.info("Clear cache: home::rendered::0")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AdminCacheController::class.java)
    }
}
