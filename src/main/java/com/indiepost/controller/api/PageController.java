package com.indiepost.controller.api;

import com.indiepost.dto.PageDto;
import com.indiepost.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/api/pages")
public class PageController {
    private final PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/{slug}")
    public PageDto get(@PathVariable String slug) {
        return pageService.findBySlug(slug);
    }
}
