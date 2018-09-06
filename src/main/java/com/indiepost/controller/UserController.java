package com.indiepost.controller;

import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.PostUserInteraction;
import com.indiepost.dto.user.SyncAuthorizationResponse;
import com.indiepost.dto.user.UserDto;
import com.indiepost.dto.user.UserProfileDto;
import com.indiepost.exceptions.UnauthorizedException;
import com.indiepost.service.PostService;
import com.indiepost.service.PostUserInteractionService;
import com.indiepost.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final PostService postService;

    private final PostUserInteractionService interactionService;


    @Inject
    public UserController(UserService userService, PostService postService, PostUserInteractionService interactionService) {
        this.userService = userService;
        this.interactionService = interactionService;
        this.postService = postService;
    }

    @PutMapping("/{username}")
    public void updateUserProfile(@PathVariable String username, @RequestBody UserProfileDto userProfile) {
        if (!userProfile.getUsername().equals(username)) {
            throw new UnauthorizedException();
        }
        userService.update(userProfile);
    }

    @PutMapping("/sync_auth/{username}")
    public SyncAuthorizationResponse syncAuthorization(@PathVariable String username, @RequestBody UserDto userDto) {
        if (!userDto.getUsername().equals(username)) {
            throw new UnauthorizedException();
        }
        return userService.syncAuthorization(userDto);
    }

    @GetMapping("/interaction/{postId}")
    public PostUserInteraction getReadingHistory(@PathVariable Long postId) {
        return interactionService.findUsersByPostId(postId);
    }

    @GetMapping("/reading_history")
    public Timeline<PostSummaryDto> getReadingHistoryList(TimelineRequest request) {
        return postService.findReadingHistory(request);
    }

    @DeleteMapping("/reading_history/{postId}")
    public void setReadingHistoryInvisible(@PathVariable Long postId) {
        interactionService.setInvisible(postId);
    }

    @DeleteMapping("/reading_history")
    public void deleteAllHistory() {
        interactionService.setInvisibleAll();
    }

    @GetMapping("/bookmark")
    public Timeline<PostSummaryDto> getBookmarkedPosts(TimelineRequest request) {
        return postService.findBookmarks(request);
    }

    @PutMapping("/bookmark/{postId}")
    public void addBookmark(@PathVariable Long postId) {
        interactionService.addBookmark(postId);
    }

    @DeleteMapping("/bookmark/{postId}")
    public void removeBookmark(@PathVariable Long postId) {
        interactionService.removeBookmark(postId);
    }

    @DeleteMapping("/bookmark")
    public void removeAllBookmarks() {
        interactionService.removeAllUsersBookmarks();
    }

    @GetMapping("/recommendations")
    public Page<PostSummaryDto> getRecommendation(Pageable pageable) {
        return postService.recommendations(PageRequest.of(0, pageable.getPageSize()));
    }
}
