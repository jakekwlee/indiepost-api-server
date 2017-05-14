package com.indiepost.controller;

import com.indiepost.config.HomeConfig;
import com.indiepost.dto.PostDto;
import com.indiepost.dto.ssr.RenderingResponseDto;
import com.indiepost.service.PostService;
import com.indiepost.service.ServerSideRenderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    private final ServerSideRenderingService serverSideRenderingService;

    private final PostService postService;

    private final HomeConfig homeConfig;

    @Autowired
    public HomeController(ServerSideRenderingService serverSideRenderingService, PostService postService, HomeConfig homeConfig) {
        this.serverSideRenderingService = serverSideRenderingService;
        this.postService = postService;
        this.homeConfig = homeConfig;
    }

    @GetMapping(value = "/")
    public String getHome(Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderHome(model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderPost(id, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }

    @GetMapping("/page/{slug}")
    public String getPage(@PathVariable String slug, Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderPage(slug, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }

    @GetMapping("/ContentView.do")
    public String getPostByLegacyId(@RequestParam Long no) {
        PostDto postDto = postService.findByLegacyId(no);
        return "redirect:post/" + postDto.getId();
    }

    @GetMapping("/category/{categoryName}")
    public String getCategoryPage(@PathVariable String categoryName, Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderPostsByCategoryPage(categoryName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }

    @GetMapping("/tag/{tagName}")
    public String getPostsByTagName(@PathVariable String tagName, Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderPostByTagPage(tagName, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }

    @GetMapping("/search/{keyword}")
    public String search(@PathVariable String keyword, Model model, HttpServletRequest request) {
        if (homeConfig.isServerSideRendering()) {
            serverSideRenderingService.renderPostSearchResultsPage(keyword, model, request.getServletPath());
        } else {
            model.addAttribute("res", new RenderingResponseDto());
        }
        return "index";
    }
}
