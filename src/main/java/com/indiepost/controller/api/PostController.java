package com.indiepost.controller.api;

import com.indiepost.dto.*;
import com.indiepost.enums.PostEnum;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PostSummaryDto> getPosts(
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {

        return postService.findByStatus(PostEnum.Status.PUBLISH, page, maxResults, true);
    }

    @RequestMapping(value = "/related", method = RequestMethod.POST)
    public List<PostSummaryDto> getPostByIds(@RequestBody RelatedPostsRequestDto dto) {
        return postService.findByIds(dto.getPostIds());
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public List<PostSummaryDto> getPostsByCategoryId(
            @PathVariable Long id,
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.findByCategoryId(id, page, maxResults, true);
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<PostSummaryDto> getPostsByQuery(@RequestBody PostQuery query) {
        return postService.findByQuery(query, query.getPage(), query.getMaxResults(), true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PostDto getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @RequestMapping(value = "/relatedPosts", method = RequestMethod.POST)
    public List<RelatedPostResponseDto> getRelatedPosts(@RequestBody RelatedPostsRequestDto dto) {
        return postService.getRelatedPosts(dto.getPostIds(), dto.isLegacy(), dto.isMobile());
    }
}