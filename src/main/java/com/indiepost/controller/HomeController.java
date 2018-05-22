package com.indiepost.controller;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.ssr.RenderingResponseDto;
import com.indiepost.service.ServerSideRenderingService;
import com.indiepost.service.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    private final ServerSideRenderingService serverSideRenderingService;

    private final AppConfig config;

    private final SitemapService sitemapService;

    @Autowired
    public HomeController(ServerSideRenderingService serverSideRenderingService, AppConfig config, SitemapService sitemapService) {
        this.serverSideRenderingService = serverSideRenderingService;
        this.config = config;
        this.sitemapService = sitemapService;
    }

    @GetMapping("/")
    public String getHome(Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderHome(model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        // TODO add server-side rendering
        model.addAttribute("res", new RenderingResponseDto());
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPost(id, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/page/{slug}")
    public String getPage(@PathVariable String slug, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPage(slug, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/category/{categoryName}")
    public String getCategoryPage(@PathVariable String categoryName, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostsByCategoryPage(categoryName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/tag/{tagName}")
    public String getPostsByTagName(@PathVariable String tagName, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostByTagPage(tagName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/contributor/{contributorName}")
    public String getPostsByContributorName(@PathVariable String contributorName, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostByTagPage(contributorName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/search/{keyword}")
    public String search(@PathVariable String keyword, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostSearchResultsPage(keyword, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
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
