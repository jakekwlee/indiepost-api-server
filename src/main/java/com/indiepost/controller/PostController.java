package com.indiepost.controller;

import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.PostImageSetDto;
import com.indiepost.dto.UserReadDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostsRequestDto;
import com.indiepost.service.ImageService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserReadService;
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

    private final UserReadService userReadService;

    @Inject
    public PostController(PostService postService, ImageService imageService, UserReadService userReadService) {
        this.postService = postService;
        this.imageService = imageService;
        this.userReadService = userReadService;
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id) {
        return postService.findOne(id);
    }

    @GetMapping
    public List<PostSummaryDto> getPosts(Pageable pageable) {
        return postService.find(pageable);
    }

    @PostMapping
    public List<PostSummaryDto> getPosts(@RequestBody PostQuery query) {
        return postService.search(query, query.getPage(), query.getMaxResults());
    }

    @GetMapping("/category/{slug}")
    public List<PostSummaryDto> getPostsByCategoryName(@PathVariable String slug, Pageable pageable) {
        return postService.findByCategorySlug(slug, pageable);
    }

    @GetMapping("/tag/{tagName}")
    public List<PostSummaryDto> getPostsByTagName(@PathVariable String tagName, Pageable pageable) {
        return postService.findByTagName(tagName, pageable);
    }

    @GetMapping("/contributor/{fullName}")
    public List<PostSummaryDto> getPostsByContributorName(@PathVariable String fullName, Pageable pageable) {
        return postService.findByContributorFullName(fullName, pageable);
    }

    @GetMapping("/read/{userId}")
    public List<PostSummaryDto> getUserReadPostsByUserId(@PathVariable Long userId, Pageable pageable) {
        return postService.findUserReadByUserId(userId, pageable);
    }

    @DeleteMapping("/read")
    public void setUserReadPostsInvisible(@RequestBody UserReadDto dto) {
        userReadService.setInvisible(dto.getUserId(), dto.getPostId());
    }

    @DeleteMapping("/read/{userId}")
    public void setAllUserReadPostsInvisible(@PathVariable Long userId) {
        userReadService.setInvisibleAllByUserId(userId);
    }

    @GetMapping("/bookmark/{userId}")
    public List<PostSummaryDto> getBookmarkedPostsByUserId(@PathVariable Long userId, Pageable pageable) {
        return postService.findUserBookmarksByUserId(userId, pageable);
    }

    @PutMapping("/bookmark")
    public void addBookmark(@RequestBody UserReadDto dto) {
        userReadService.setBookmark(dto.getUserId(), dto.getPostId());
    }

    @DeleteMapping("/bookmark")
    public void removeBookmark(@RequestBody UserReadDto dto) {
        userReadService.unsetBookmark(dto.getUserId(), dto.getPostId());
    }

    @PostMapping("/search")
    public List<PostSummaryDto> fullTextSearch(@RequestBody FullTextSearchQuery query) {
        return postService.fullTextSearch(query);
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
