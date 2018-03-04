package com.indiepost.controller.api;

import com.indiepost.dto.*;
import com.indiepost.service.ImageService;
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

    private final ImageService imageService;

    @Autowired
    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping
    public List<PostSummary> getPosts(
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {

        return postService.findAll(page, maxResults, true);
    }

    @GetMapping("/category/{id}")
    public List<PostSummary> getPostsByCategoryId(
            @PathVariable Long id,
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.findByCategoryId(id, page, maxResults, true);
    }

    @GetMapping("/tag/{tagName}")
    public List<PostSummary> getPostsByTagName(
            @PathVariable String tagName) {
        return postService.findByTagName(tagName);
    }

    @PostMapping("/search")
    public List<PostSummary> getPosts(@RequestBody FullTextSearchQuery query) {
        return postService.fullTextSearch(query);
    }

    @PostMapping
    public List<PostSummary> getPostsByQuery(@RequestBody PostQuery query) {
        return postService.findByQuery(query, query.getPage(), query.getMaxResults(), true);
    }

    @PostMapping("/related")
    public List<PostSummary> getPostByIds(@RequestBody RelatedPostsRequestDto dto) {
        return postService.findByIds(dto.getPostIds());
    }

    @PostMapping(value = "/relatedPosts")
    public List<RelatedPostResponseDto> getRelatedPosts(@RequestBody RelatedPostsRequestDto dto) {
        return postService.getRelatedPosts(dto.getPostIds(), dto.isLegacy(), dto.isMobile());
    }

    @GetMapping(value = "/images/{id}")
    public PostImageSetListDto getImagesOnPost(@PathVariable Long id) {
        return imageService.findImagesOnPost(id);
    }

    @GetMapping(value = "/future")
    public List<PostSummary> getScheduledPosts() {
        return postService.getScheduledPosts();
    }
}
