package com.indiepost.controller

import com.indiepost.dto.post.PostSummaryDto
import com.indiepost.dto.post.PostUserInteraction
import com.indiepost.dto.user.SyncAuthorizationResponse
import com.indiepost.dto.user.UserDto
import com.indiepost.dto.user.UserProfileDto
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.service.PostService
import com.indiepost.service.PostUserInteractionService
import com.indiepost.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RestController
@RequestMapping("/user")
class UserController @Inject constructor(
        private val userService: UserService,
        private val postService: PostService,
        private val interactionService: PostUserInteractionService) {

    @PutMapping("/{username}")
    fun updateUserProfile(@PathVariable username: String, @RequestBody userProfile: UserProfileDto) {
        if (userProfile.username != username) {
            throw UnauthorizedException()
        }
        userService.update(userProfile)
    }

    @PutMapping("/sync_auth/{username}")
    fun syncAuthorization(@PathVariable username: String, @RequestBody userDto: UserDto): SyncAuthorizationResponse {
        if (userDto.username != username) {
            throw UnauthorizedException()
        }
        return userService.syncAuthorization(userDto)
    }

    @GetMapping("/interaction/{postId}")
    fun getReadingHistory(@PathVariable postId: Long): PostUserInteraction? {
        return interactionService.findUsersByPostId(postId)
    }

    @GetMapping("/reading_history")
    fun getReadingHistoryList(pageable: Pageable): Page<PostSummaryDto> {
        return postService.findReadingHistory(pageable)
    }

    @DeleteMapping("/reading_history/{postId}")
    fun setReadingHistoryInvisible(@PathVariable postId: Long) {
        interactionService.setInvisible(postId)
    }

    @DeleteMapping("/reading_history")
    fun deleteAllHistory() {
        interactionService.setInvisibleAll()
    }

    @GetMapping("/bookmark")
    fun getBookmarkedPosts(pageable: Pageable): Page<PostSummaryDto> {
        return postService.findBookmarks(pageable)
    }

    @PutMapping("/bookmark/{postId}")
    fun addBookmark(@PathVariable postId: Long) {
        interactionService.addBookmark(postId)
    }

    @DeleteMapping("/bookmark/{postId}")
    fun removeBookmark(@PathVariable postId: Long) {
        interactionService.removeBookmark(postId)
    }

    @DeleteMapping("/bookmark")
    fun removeAllBookmarks() {
        interactionService.removeAllUsersBookmarks()
    }

    @GetMapping("/recommendations")
    fun getRecommendation(pageable: Pageable): Page<PostSummaryDto> {
        return postService.recommendations(PageRequest.of(0, pageable.pageSize))
    }
}
