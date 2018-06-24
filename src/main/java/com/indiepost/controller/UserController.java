package com.indiepost.controller;

import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.UserDto;
import com.indiepost.dto.UserProfileDto;
import com.indiepost.dto.post.PostInteractionDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.service.PostInteractionService;
import com.indiepost.service.PostService;
import com.indiepost.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final PostInteractionService postInteractionService;

    private final PostService postService;

    @Inject
    public UserController(UserService userService, PostInteractionService postInteractionService, PostService postService) {
        this.userService = userService;
        this.postInteractionService = postInteractionService;
        this.postService = postService;
    }

    @PutMapping("/{username}")
    public UserProfileDto sync(@PathVariable String username, @RequestBody UserDto userDto) {
        if (!userDto.getUsername().equals(username)) {
            //TODO handle error
            return null;
        }
        return userService.sync(userDto);
    }

    @GetMapping("/readingHistory")
    public Timeline<PostSummaryDto> getReadingHistoryList(TimelineRequest request) {
        return postService.findReadingHistory(request);
    }

    @GetMapping("/interaction/{postId}")
    public PostInteractionDto getReadingHistory(@PathVariable Long postId) {
        return postInteractionService.findUsersByPostId(postId);
    }

    @DeleteMapping("/readingHistory/{postId}")
    public void setReadingHistoryInvisible(@PathVariable Long postId) {
        postInteractionService.setInvisible(postId);
    }

    @DeleteMapping("/readingHistory")
    public void deleteAllHistory() {
        postInteractionService.setInvisibleAll();
    }

    @GetMapping("/bookmark")
    public Timeline<PostSummaryDto> getBookmarkedPosts(TimelineRequest request) {
        return postService.findBookmarks(request);
    }

    @PutMapping("/bookmark/{postId}")
    public void addBookmark(@PathVariable Long postId) {
        postInteractionService.setBookmark(postId);
    }

    @DeleteMapping("/bookmark/{postId}")
    public void removeBookmark(@PathVariable Long postId) {
        postInteractionService.unsetBookmark(postId);
    }

}
