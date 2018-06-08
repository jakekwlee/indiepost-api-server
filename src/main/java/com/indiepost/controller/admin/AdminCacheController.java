package com.indiepost.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/cache")
public class AdminCacheController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCacheController.class);

    @GetMapping("/post/{id}")
    @CacheEvict(cacheNames = "post::rendered", key = "#id")
    public void clearPostCache(@PathVariable Long id) {
        logger.info("Clear cache: post::rendered::" + id);
    }

    @GetMapping("/page/{slug}")
    @CacheEvict(cacheNames = "static-page::rendered", key = "#slug")
    public void clearStaticPageCache(@PathVariable String slug) {
        logger.info("Clear cache: static-page::rendered::" + slug);
    }

    @GetMapping("/category/{slug}")
    @CacheEvict(cacheNames = "category::rendered", key = "#slug")
    public void clearCategoryCache(@PathVariable String slug) {
        logger.info("Clear cache: category::rendered::" + slug);
    }

    @GetMapping("/home")
    @CacheEvict(cacheNames = "home::rendered", key = "0")
    public void clearHomepageCache() {
        logger.info("Clear cache: home::rendered::0");
    }
}
