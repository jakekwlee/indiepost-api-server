package com.indiepost.controller;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.service.StaticPageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/pages")
public class StaticPageController {
    private final StaticPageService staticPageService;

    @Inject
    public StaticPageController(StaticPageService staticPageService) {
        this.staticPageService = staticPageService;
    }

    @GetMapping("/{slug}")
    public StaticPageDto get(@PathVariable String slug) {
        return staticPageService.findBySlug(slug);
    }
}
