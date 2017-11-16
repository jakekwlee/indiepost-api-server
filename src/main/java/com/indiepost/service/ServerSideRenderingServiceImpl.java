package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.InitialData;
import com.indiepost.dto.PageDto;
import com.indiepost.dto.ServerSideRenderingRequest;
import com.indiepost.dto.ServerSideRenderingResponse;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jake on 17. 5. 13.
 */
@Service
public class ServerSideRenderingServiceImpl implements ServerSideRenderingService {

    private final InitialDataService initialDataService;

    private final PostService postService;

    private final PageService pageService;

    private final RestTemplate restTemplate;

    private final AppConfig config;

    @Autowired
    public ServerSideRenderingServiceImpl(InitialDataService initialDataService, PostService postService, PageService pageService, RestTemplate restTemplate, AppConfig config) {
        this.initialDataService = initialDataService;
        this.postService = postService;
        this.pageService = pageService;
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public void renderHome(Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(true);
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, servletPath);

        render(model, rsRequest);
    }

    @Override
    public void renderPost(Long postId, Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(false);
        PostDto postDto = postService.findOne(postId);
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, postDto, servletPath);
        this.render(model, rsRequest);
    }

    @Override
    public void renderPage(String pageSlug, Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(false);
        PageDto pageDto = pageService.findBySlug(pageSlug);
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, pageDto, servletPath);
        this.render(model, rsRequest);
    }

    @Override
    public void renderPostsByCategoryPage(String categorySlug, Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(false);
        PostQuery query = new PostQuery();
        query.setCategorySlug(categorySlug.toLowerCase());
        query.setPage(0);
        query.setMaxResults(config.getFetchCount());
        List<PostSummaryDto> posts = postService.findByQuery(query, true);
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, posts, servletPath);
        this.render(model, rsRequest);
    }

    @Override
    public void renderPostByTagPage(String tagName, Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(false);
        List<PostSummaryDto> posts = postService.findByTagName(tagName);
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, posts, servletPath);
        this.render(model, rsRequest);
    }

    @Override
    public void renderPostSearchResultsPage(String keyword, Model model, String servletPath) {
        InitialData initialData = initialDataService.getInitialData(false);
        List<PostSummaryDto> posts = postService.search(keyword, 0, config.getFetchCount());
        ServerSideRenderingRequest rsRequest =
                new ServerSideRenderingRequest(initialData, posts, servletPath);
        this.render(model, rsRequest);
    }

    private void render(Model model, ServerSideRenderingRequest requestDto) {
        try {
            ServerSideRenderingResponse response = restTemplate.postForObject(
                    config.getRenderingServerUrl() + requestDto.getPath(),
                    requestDto,
                    ServerSideRenderingResponse.class
            );
            model.addAttribute("res", response);
        } catch (RestClientException e) {
            e.printStackTrace();
            model.addAttribute("res", new ServerSideRenderingResponse());
        }
        model.addAttribute("fetchCount", config.getFetchCount());
    }
}
