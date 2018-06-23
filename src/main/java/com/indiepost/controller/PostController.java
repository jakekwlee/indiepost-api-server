package com.indiepost.controller;

import com.indiepost.dto.PostImageSetDto;
import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.UserReadDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostsRequestDto;
import com.indiepost.enums.Types;
import com.indiepost.service.ImageService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserReadingService;
import org.springframework.data.domain.Page;
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

    private final UserReadingService userReadingService;

    @Inject
    public PostController(PostService postService, ImageService imageService, UserReadingService userReadingService) {
        this.postService = postService;
        this.imageService = imageService;
        this.userReadingService = userReadingService;
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

    @GetMapping("/readingHistory/{userId}")
    public Timeline<PostSummaryDto> getUserReadPostsByUserId(@PathVariable Long userId, TimelineRequest request) {
        return postService.findReadingHistoryByUserId(userId, request);
    }

    @DeleteMapping("/readingHistory")
    public void setUserReadPostsInvisible(@RequestBody UserReadDto dto) {
        userReadingService.setInvisible(dto.getUserId(), dto.getPostId());
    }

    @DeleteMapping("/readingHistory/{userId}")
    public void setAllUserReadPostsInvisible(@PathVariable Long userId) {
        userReadingService.setInvisibleAllByUserId(userId);
    }

    @GetMapping("/bookmark/{userId}")
    public Timeline<PostSummaryDto> getBookmarkedPostsByUserId(@PathVariable Long userId, TimelineRequest request) {
        return postService.findBookmarksByUserId(userId, request);
    }

    @PutMapping("/bookmark")
    public void addBookmark(@RequestBody UserReadDto dto) {
        userReadingService.setBookmark(dto.getUserId(), dto.getPostId());
    }

    @DeleteMapping("/bookmark")
    public void removeBookmark(@RequestBody UserReadDto dto) {
        userReadingService.unsetBookmark(dto.getUserId(), dto.getPostId());
    }

    @GetMapping("/search/{searchText}")
    public Page<PostSummaryDto> fullTextSearch(@PathVariable String searchText, Pageable pageable) {
        return postService.fullTextSearch(searchText, pageable);
    }

    @PostMapping("/related")
    public List<PostSummaryDto> getPostByIds(@RequestBody RelatedPostsRequestDto dto) {
        return postService.findByIds(dto.getPostIds());
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
