package com.indiepost.controller;

import com.indiepost.service.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * Created by jake on 17. 1. 4.
 */
public class HomeController {

    private final SitemapService sitemapService;

    @Autowired
    public HomeController(SitemapService sitemapService) {
        this.sitemapService = sitemapService;
    }

    @GetMapping(value = "/sitemap.xml", produces = APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        return sitemapService.buildSitemap();
    }

    @GetMapping(value = "/robots.txt", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robotsTxt() {
        return "User-agent: *\n" +
                "Disallow: /admin/\n" +
                "Disallow: /indiepost/\n" +
                "Sitemap: https://www.indiepsot.co.kr/sitemap.xml";
    }
}
