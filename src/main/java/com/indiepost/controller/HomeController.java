package com.indiepost.controller;

import com.indiepost.config.HomeConfig;
import com.indiepost.dto.*;
import com.indiepost.service.InitialDataService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jake on 17. 1. 4.
 */
@Controller
public class HomeController {

    private final InitialDataService initialDataService;

    private final PostService postService;

    private final RestTemplate restTemplate;

    private final HomeConfig homeConfig;

    @Autowired
    public HomeController(InitialDataService initialDataService, PostService postService, RestTemplate restTemplate, HomeConfig homeConfig) {
        this.initialDataService = initialDataService;
        this.postService = postService;
        this.restTemplate = restTemplate;
        this.homeConfig = homeConfig;
    }

    @GetMapping(value = {"/", "/page/**", "/archive/**"})
    public String getHome(Model model, HttpServletRequest request) {
        InitialData initialData = initialDataService.getInitialData(true);
        RenderingRequestDto rsRequest =
                new RenderingRequestDto(initialData, request.getServletPath());

        this.render(model, rsRequest);
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        InitialData initialData = initialDataService.getInitialData(false);
        PostDto postDto = postService.findById(id);
        RenderingRequestDto rsRequest =
                new RenderingRequestDto(initialData, postDto, request.getServletPath());
        this.render(model, rsRequest);
        return "index";
    }

    @GetMapping("/ContentView.do")
    public String getPostByLegacyId(@RequestParam Long no, Model model, HttpServletRequest request) {
        InitialData initialData = initialDataService.getInitialData(false);
        PostDto postDto = postService.findByLegacyId(no);
        RenderingRequestDto rsRequest
                = new RenderingRequestDto(initialData, postDto, request.getServletPath());
        this.render(model, rsRequest);
        return "index";
    }

    @GetMapping("/category/{categoryName}")
    public String getCategoryPage(@PathVariable String categoryName, Model model, HttpServletRequest request) {
        InitialData initialData = initialDataService.getInitialData(false);
        PostQuery query = new PostQuery();
        query.setCategorySlug(categoryName.toLowerCase());
        List<PostSummaryDto> posts = postService.findByQuery(query, 0, homeConfig.getFetchCount(), true);
        RenderingRequestDto rsRequest =
                new RenderingRequestDto(initialData, posts, request.getServletPath());
        this.render(model, rsRequest);
        return "index";
    }

    private void render(Model model, RenderingRequestDto requestDto) {
        try {
            RenderingResponseDto response = restTemplate.postForObject(
                    homeConfig.getRenderingServerUri() + requestDto.getPath(),
                    requestDto,
                    RenderingResponseDto.class
            );
            model.addAttribute("res", response);
        } catch (RestClientException e) {
            e.printStackTrace();
            model.addAttribute("res", new RenderingResponseDto());
        }
        model.addAttribute("fetchCount", homeConfig.getFetchCount());
    }


}
