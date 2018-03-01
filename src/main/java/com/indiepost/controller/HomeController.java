package com.indiepost.controller;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.ServerSideRenderingResponse;
import com.indiepost.dto.post.PostDto;
import com.indiepost.service.PostService;
import com.indiepost.service.ServerSideRenderingService;
import com.indiepost.service.SitemapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    private final ServerSideRenderingService serverSideRenderingService;

    private final PostService postService;

    private final AppConfig config;

    private final SitemapService sitemapService;

    @Inject
    public HomeController(ServerSideRenderingService serverSideRenderingService, PostService postService, AppConfig config, SitemapService sitemapService) {
        this.serverSideRenderingService = serverSideRenderingService;
        this.postService = postService;
        this.config = config;
        this.sitemapService = sitemapService;
    }

    @GetMapping("/")
    public String getHome(Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderHome(model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/oauth2/callback")
    public String getOAuth2Callback(Model model) {
        // TODO
        model.addAttribute("res", new ServerSideRenderingResponse());
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPost(id, model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/page/{slug}")
    public String getPage(@PathVariable String slug, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPage(slug, model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/ContentView.do")
    public String getPostByLegacyId(@RequestParam Long no) {
        PostDto postDto = postService.findOneByLegacyId(no);
        return "redirect:post/" + postDto.getId();
    }

    @GetMapping("/category/{categoryName}")
    public String getCategoryPage(@PathVariable String categoryName, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostsByCategoryPage(categoryName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/tag/{tagName}")
    public String getPostsByTagName(@PathVariable String tagName, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostByTagPage(tagName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("cdnUrl", config.getCdnUrl());
        return "index";
    }

    @GetMapping("/search/{keyword}")
    public String search(@PathVariable String keyword, Model model, HttpServletRequest request) {
        if (config.isServerSideRendering()) {
            serverSideRenderingService.renderPostSearchResultsPage(keyword, model, request.getServletPath());
        } else {
            model.addAttribute("res", new ServerSideRenderingResponse());
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
                "Sitemap: http://www.indiepsot.co.kr/sitemap.xml";
    }
}
