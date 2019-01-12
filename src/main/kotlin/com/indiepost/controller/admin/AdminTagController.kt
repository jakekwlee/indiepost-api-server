package com.indiepost.controller.admin

import com.indiepost.service.TagService
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RestController
@RequestMapping("/admin/tags")
class AdminTagController @Inject constructor(private val tagService: TagService) {

    @PostMapping("/selected")
    @CacheEvict(cacheNames = ["tag::rendered", "post::rendered", "home::rendered", "static-page::rendered"], allEntries = true)
    fun updateSelectedTags(@RequestBody tags: List<String>) {
        logger.info("Clear all caches")
        tagService.updateSelected(tags)
    }

    @GetMapping("/selected")
    fun getSelectedTags(): List<String> {
        return tagService.findSelected()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AdminTagController::class.java)
    }
}
