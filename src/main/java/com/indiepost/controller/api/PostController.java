package com.indiepost.controller.api;

import com.indiepost.dto.PostImageSetDto;
import com.indiepost.dto.post.*;
import com.indiepost.service.ImageService;
import com.indiepost.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    private final ImageService imageService;

    @Inject
    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id) {
        return postService.findOne(id);
    }

    @GetMapping
    public List<PostSummaryDto> getPosts(
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.find(page, maxResults, true);
    }

    @PostMapping
    public List<PostSummaryDto> getPosts(@RequestBody PostQuery query) {
        return postService.search(query, query.getPage(), query.getMaxResults(), false);
    }

    @GetMapping("/category/{id}")
    public List<PostSummaryDto> getPostsByCategoryId(
            @PathVariable Long id,
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.findByCategoryId(id, page, maxResults, true);
    }

    @GetMapping("/tag/{tagName}")
    public List<PostSummaryDto> getPostsByTagName(
            @PathVariable String tagName,
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.findByTagName(tagName, page, maxResults, true);
    }

    @PostMapping("/search")
    public List<PostSummaryDto> fullTextSearch(@RequestBody FullTextSearchQuery query) {
        return postService.fullTextSearch(query);
    }

    @PostMapping("/related")
    public List<PostSummaryDto> getPostByIds(@RequestBody RelatedPostsRequest dto) {
        return postService.findByIds(dto.getPostIds());
    }

    @PostMapping(value = "/relatedPosts")
    public List<RelatedPostResponse> getRelatedPosts(@RequestBody RelatedPostsRequest dto) {
        return postService.findRelatedPosts(dto.getPostIds(), dto.isLegacy(), dto.isMobile());
    }

    @GetMapping(value = "/images/{id}")
    public PostImageSetDto getImagesOnPost(@PathVariable Long id) {
        return imageService.findImagesOnPost(id);
    }

    @GetMapping(value = "/future")
    public List<PostSummaryDto> getScheduledPosts() {
        return postService.findScheduledPosts();
    }
}
