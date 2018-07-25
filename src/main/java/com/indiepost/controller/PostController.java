package com.indiepost.controller;

import com.indiepost.dto.PostImageSetDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import com.indiepost.service.ImageService;
import com.indiepost.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/posts")
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
    public Page<PostSummaryDto> getPosts(@RequestParam Boolean splash,
                                         @RequestParam Boolean featured,
                                         @RequestParam Boolean picked,
                                         Pageable pageable) {
        PostQuery postQuery = new PostQuery.Builder(Types.PostStatus.PUBLISH)
                .splash(splash)
                .featured(featured)
                .picked(picked)
                .build();
        return postService.query(postQuery, pageable);
    }

    @GetMapping("/category/{slug}")
    public Page<PostSummaryDto> getPostsByCategoryName(@PathVariable String slug, Pageable pageable) {
        return postService.findByCategorySlug(slug, pageable);
    }

    @GetMapping("/tag/{tagName}")
    public Page<PostSummaryDto> getPostsByTagName(@PathVariable String tagName, Pageable pageable) {
        return postService.findByTagName(tagName, pageable);
    }

    @GetMapping("/contributor/{fullName}")
    public Page<PostSummaryDto> getPostsByContributorName(@PathVariable String fullName, Pageable pageable) {
        return postService.findByContributorFullName(fullName, pageable);
    }

    @GetMapping("/search/{searchText}")
    public Page<PostSummaryDto> fullTextSearch(@PathVariable String searchText, Pageable pageable) {
        return postService.fullTextSearch(searchText, pageable);
    }

    @GetMapping("/related/{id}")
    public Page<PostSummaryDto> getRelatedPosts(@PathVariable Long id, Pageable pageable) {
        // ignore pageable for now...
        return postService.findRelatedPostsById(id, PageRequest.of(0, 4));
    }

    @GetMapping("/more-like-this/{id}")
    public Page<PostSummaryDto> getMoreLikeThis(@PathVariable Long id, Pageable pageable) {
        return postService.moreLikeThis(id, PageRequest.of(0, 8));
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
