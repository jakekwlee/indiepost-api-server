package com.indiepost.controller.api;

import com.indiepost.dto.PostImageSetDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostResponse;
import com.indiepost.dto.post.RelatedPostsRequest;
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
        return postService.findOne(id);
    }

    @GetMapping
    public List<PostSummaryDto> getPosts(
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {

        return postService.find(page, maxResults, true);
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

    @GetMapping("/search")
    public List<PostSummaryDto> getPosts(
            @RequestParam("q") String text,
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults) {
        return postService.search(text, page, maxResults);
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
