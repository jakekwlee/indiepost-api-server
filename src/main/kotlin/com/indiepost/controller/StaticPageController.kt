package com.indiepost.controller

import com.indiepost.dto.StaticPageDto
import com.indiepost.service.StaticPageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.inject.Inject

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/pages")
class StaticPageController @Inject
constructor(private val staticPageService: StaticPageService) {

    @GetMapping("/{slug}")
    fun getStaticPage(@PathVariable slug: String): StaticPageDto? {
        return staticPageService.findBySlug(slug)
    }
}
